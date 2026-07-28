package com.kirin.bilitv.core.network

import android.util.Log
import com.kirin.bilitv.core.auth.WbiKeyRepository
import com.kirin.bilitv.core.auth.WbiSigner
import com.kirin.bilitv.core.model.VideoSummary
import com.kirin.bilitv.core.storage.SessionStore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import java.util.LinkedHashMap

internal class SearchVideoRepository(
  private val apiClient: BiliApiClient,
  private val wbiKeyRepository: WbiKeyRepository,
  private val wbiSigner: WbiSigner,
  private val sessionStore: SessionStore,
) {
  private val pgcSearchMetadataCache = object : LinkedHashMap<String, VideoSummary>(
    PgcSearchMetadataCacheMaxSize,
    PgcSearchMetadataCacheLoadFactor,
    true,
  ) {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, VideoSummary>?): Boolean {
      return size > PgcSearchMetadataCacheMaxSize
    }
  }

  suspend fun searchVideos(
    keyword: String,
    page: Int,
    order: String,
    searchType: SearchContentType = SearchContentType.Video,
    enrichPgcSearch: Boolean = true,
  ): List<VideoSummary> {
    if (keyword.isBlank()) return emptyList()

    val sessData = sessionStore.sessData.first()
    val keys = wbiKeyRepository.ensureKeys(sessData)
    val params = mutableMapOf(
      "keyword" to keyword,
      "search_type" to searchType.apiType,
      "page" to page.toString(),
      "pagesize" to "20",
      "order" to order,
    )

    val signedParams = if (keys != null) {
      wbiSigner.sign(params, keys.imgKey, keys.subKey)
    } else {
      params
    }

    val result = runCatching {
      val signedRoot = apiClient.getJson(
        url = BiliApiEndpoints.Search,
        params = signedParams,
        sessData = sessData,
      ).rootObject()
      signedRoot.requireBiliCodeOk("search")
      signedRoot.searchResultOrNull()
    }.getOrNull()
      ?: runCatching {
        val unsignedRoot = apiClient.getJson(
          url = BiliApiEndpoints.SearchLegacy,
          params = params,
        ).rootObject()
        unsignedRoot.requireBiliCodeOk("search fallback")
        unsignedRoot.searchResultOrNull()
      }.getOrNull()
      ?: return emptyList()

    val summaries = result
      .mapNotNull { it.asObjectOrNull() }
      .mapNotNull { item ->
        when (searchType) {
          SearchContentType.Video -> item
            .takeIf { it.string("bvid").isNotBlank() }
            ?.let(VideoSummaryMappers::fromSearch)
          SearchContentType.Bangumi -> item
            .takeIf {
              it.long("season_id") > 0L ||
                it.long("pgc_season_id") > 0L ||
                it.long("media_id") > 0L
            }
            ?.let(VideoSummaryMappers::fromPgcSearch)
        }
      }
    return when (searchType) {
      SearchContentType.Video -> summaries
      SearchContentType.Bangumi -> {
        val cachedSummaries = summaries.applyPgcSearchMetadataCache()
        Log.i(
          SearchLogTag,
          "pgc search parsed count=${cachedSummaries.size} first=" +
            cachedSummaries.firstOrNull()?.toSearchLogText().orEmpty(),
        )
        if (enrichPgcSearch) enrichPgcSearchSummaries(cachedSummaries, sessData) else cachedSummaries
      }
    }
  }

  suspend fun enrichPgcSearchVideos(summaries: List<VideoSummary>): List<VideoSummary> {
    if (summaries.isEmpty()) return emptyList()
    return enrichPgcSearchSummaries(
      summaries = summaries,
      sessData = sessionStore.sessData.first(),
    )
  }

  suspend fun getSearchSuggestions(keyword: String): List<String> {
    if (keyword.isBlank()) return emptyList()

    val root = apiClient.getJson(
      url = BiliApiEndpoints.SearchSuggest,
      params = mapOf(
        "term" to keyword,
        "main_ver" to "v1",
        "highlight" to "",
      ),
    ).rootObject()
    root.requireBiliCodeOk("search suggestions")

    val tags = root.obj("result")?.get("tag") as? JsonArray ?: return emptyList()
    return tags
      .mapNotNull { it.asObjectOrNull()?.string("value") }
      .filter { it.isNotBlank() }
  }

  private fun JsonObject.searchResultOrNull(): JsonArray? {
    return obj("data")?.get("result") as? JsonArray
  }

  private suspend fun enrichPgcSearchSummaries(
    summaries: List<VideoSummary>,
    sessData: String?,
  ): List<VideoSummary> = coroutineScope {
    val semaphore = Semaphore(PgcSearchMetadataConcurrency)
    summaries.map { summary ->
      async {
        val cachedSummary = summary.cachedPgcSearchMetadataOrNull()
        if (cachedSummary != null) {
          Log.i(SearchLogTag, "pgc enrich cache hit ${cachedSummary.toSearchLogText()}")
          return@async cachedSummary
        }
        semaphore.withPermit {
          runCatching { enrichPgcSearchSummary(summary, sessData) }
            .onSuccess { enriched -> cachePgcSearchMetadata(enriched) }
            .onFailure { error ->
              Log.w(
                SearchLogTag,
                "pgc enrich failed ${summary.toSearchLogText()} " +
                  "error=${error.javaClass.simpleName}: ${error.message.orEmpty().take(SearchLogErrorMaxLength)}",
              )
            }
            .getOrDefault(summary)
        }
      }
    }.awaitAll()
  }

  private fun List<VideoSummary>.applyPgcSearchMetadataCache(): List<VideoSummary> {
    return map { summary -> summary.cachedPgcSearchMetadataOrNull() ?: summary }
  }

  private fun VideoSummary.cachedPgcSearchMetadataOrNull(): VideoSummary? {
    val cached = synchronized(pgcSearchMetadataCache) {
      pgcSearchCacheKeys().firstNotNullOfOrNull { key -> pgcSearchMetadataCache[key] }
    } ?: return null
    return mergePgcSearchMetadata(cached)
  }

  private fun VideoSummary.mergePgcSearchMetadata(cached: VideoSummary): VideoSummary {
    return cached.copy(
      title = title,
      pic = pic.ifBlank { cached.pic },
      ownerName = cached.ownerName.ifBlank { ownerName },
      ownerFace = cached.ownerFace.ifBlank { ownerFace },
      ownerMid = if (cached.ownerMid > 0L) cached.ownerMid else ownerMid,
      pubdate = if (cached.pubdate > 0L) cached.pubdate else pubdate,
      badge = badge.ifBlank { cached.badge },
      progress = progress,
      viewAt = viewAt,
      historyPage = historyPage,
      historyPart = historyPart,
      historyVideos = historyVideos,
      isLive = isLive,
      pgcTypeName = pgcTypeName.ifBlank { cached.pgcTypeName },
      pgcIndexShow = pgcIndexShow.ifBlank { cached.pgcIndexShow },
    )
  }

  private fun cachePgcSearchMetadata(video: VideoSummary) {
    val keys = video.pgcSearchCacheKeys()
    if (keys.isEmpty()) return
    synchronized(pgcSearchMetadataCache) {
      keys.forEach { key -> pgcSearchMetadataCache[key] = video }
    }
  }

  private suspend fun enrichPgcSearchSummary(
    summary: VideoSummary,
    sessData: String?,
  ): VideoSummary {
    if (summary.pgcSeasonId <= 0L && summary.pgcEpisodeId <= 0L) {
      return summary
    }
    val params = when {
      summary.pgcSeasonId > 0L -> mapOf("season_id" to summary.pgcSeasonId.toString())
      summary.pgcEpisodeId > 0L -> mapOf("ep_id" to summary.pgcEpisodeId.toString())
      else -> return summary
    }
    val root = apiClient.getJson(
      url = BiliApiEndpoints.PgcSeason,
      params = params,
      sessData = sessData,
    ).rootObject()
    root.requireBiliCodeOk("pgc search season")

    val result = root.obj("result") ?: return summary
    val episodes = result["episodes"] as? JsonArray
    val selectedEpisode = episodes
      ?.mapNotNull { it.asObjectOrNull() }
      ?.firstOrNull { episode ->
        (summary.pgcEpisodeId > 0L && (episode.long("ep_id") == summary.pgcEpisodeId || episode.long("id") == summary.pgcEpisodeId)) ||
          (summary.cid > 0L && episode.long("cid") == summary.cid)
      }
      ?: episodes
        ?.firstOrNull()
        ?.asObjectOrNull()
    val stat = result.obj("stat")
    val upInfo = result.obj("up_info")
    val seasonPubdate = result.long("pub_time").takeIf { it > 0L }
      ?: result.long("publish").takeIf { it > 0L }
      ?: summary.pubdate
    val viewCount = BiliNumberParser.toLong(
      stat?.get("views")
        ?: stat?.get("view")
        ?: stat?.get("play")
        ?: stat?.get("plays"),
    ).takeIf { it > 0L }
      ?: BiliNumberParser.parseCountText(result.string("subtitle")).takeIf { it > 0L }
      ?: selectedEpisode?.string("subtitle")?.let(BiliNumberParser::parseCountText)?.takeIf { it > 0L }
      ?: summary.view
    val danmakuCount = BiliNumberParser.toInt(
      stat?.get("danmakus")
        ?: stat?.get("danmaku")
        ?: stat?.get("danmaku_count")
        ?: stat?.get("dm"),
    ).takeIf { it > 0 } ?: summary.danmaku

    val enriched = summary.copy(
      bvid = selectedEpisode?.string("bvid").orEmpty().ifBlank { summary.bvid },
      cid = selectedEpisode?.long("cid")?.takeIf { it > 0L } ?: summary.cid,
      ownerName = upInfo?.string("uname").orEmpty().ifBlank { summary.ownerName },
      ownerFace = VideoSummaryMappers.fixPicUrl(
        upInfo?.string("avatar").orEmpty()
          .ifBlank { summary.ownerFace }
          .ifBlank { summary.pic },
      ),
      view = viewCount,
      danmaku = danmakuCount,
      duration = 0,
      pubdate = seasonPubdate,
      pgcEpisodeId = selectedEpisode
        ?.long("ep_id")
        ?.takeIf { it > 0L }
        ?: selectedEpisode?.long("id")?.takeIf { it > 0L }
        ?: summary.pgcEpisodeId,
    )
    Log.i(
      SearchLogTag,
      "pgc enrich title=${summary.title.take(SearchLogTitleMaxLength)} " +
        "seasonId=${summary.pgcSeasonId} epId=${enriched.pgcEpisodeId} " +
        "view=${enriched.view} danmaku=${enriched.danmaku}",
    )
    return enriched
  }

}

enum class SearchContentType(val apiType: String) {
  Video("video"),
  Bangumi("media_bangumi"),
}

private const val PgcSearchMetadataConcurrency = 4
private const val PgcSearchMetadataCacheMaxSize = 128
private const val PgcSearchMetadataCacheLoadFactor = 0.75f
private const val SearchLogTag = "BiliTVNative:Search"
private const val SearchLogTitleMaxLength = 24
private const val SearchLogErrorMaxLength = 120

private fun VideoSummary.pgcSearchCacheKeys(): List<String> {
  return buildList {
    if (pgcSeasonId > 0L) add("season-$pgcSeasonId")
    if (pgcEpisodeId > 0L) add("episode-$pgcEpisodeId")
  }
}

private fun VideoSummary.toSearchLogText(): String {
  return "title=${title.take(SearchLogTitleMaxLength)} " +
    "seasonId=$pgcSeasonId epId=$pgcEpisodeId view=$view danmaku=$danmaku"
}
