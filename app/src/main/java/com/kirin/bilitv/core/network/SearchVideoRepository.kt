package com.kirin.bilitv.core.network

import com.kirin.bilitv.core.auth.WbiKeyRepository
import com.kirin.bilitv.core.auth.WbiSigner
import com.kirin.bilitv.core.model.VideoSummary
import com.kirin.bilitv.core.storage.SessionStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

internal class SearchVideoRepository(
  private val apiClient: BiliApiClient,
  private val wbiKeyRepository: WbiKeyRepository,
  private val wbiSigner: WbiSigner,
  private val sessionStore: SessionStore,
) {
  suspend fun searchVideos(
    keyword: String,
    page: Int,
    order: String,
    searchType: SearchContentType = SearchContentType.Video,
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

    return result
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

}

enum class SearchContentType(val apiType: String) {
  Video("video"),
  Bangumi("media_bangumi"),
}
