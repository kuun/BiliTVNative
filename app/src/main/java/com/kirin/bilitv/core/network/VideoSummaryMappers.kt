package com.kirin.bilitv.core.network

import com.kirin.bilitv.core.model.VideoSummary
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

internal object VideoSummaryMappers {
  fun fromArchive(json: JsonObject): VideoSummary {
    val owner = json.obj("owner")
    val stat = json.obj("stat")
    return VideoSummary(
      bvid = json.string("bvid"),
      title = json.string("title"),
      pic = fixPicUrl(json.string("pic")),
      ownerName = owner?.string("name").orEmpty(),
      ownerFace = fixPicUrl(owner?.string("face").orEmpty()),
      ownerMid = owner?.long("mid") ?: 0L,
      view = BiliNumberParser.toLong(stat?.get("view")),
      danmaku = BiliNumberParser.toInt(stat?.get("danmaku")),
      duration = BiliNumberParser.parseDuration(json["duration"]),
      pubdate = json.long("pubdate"),
      badge = filterBadge(json.string("badge")),
    )
  }

  fun fromDynamicItem(json: JsonObject): VideoSummary? {
    if (json["visible"]?.jsonPrimitive?.booleanOrNull == false) {
      return null
    }

    val modules = json.obj("modules") ?: return null
    val dynamicModule = modules.obj("module_dynamic") ?: return null
    val major = dynamicModule.obj("major") ?: return null
    if (major.string("type") != "MAJOR_TYPE_ARCHIVE") {
      return null
    }

    val archive = major.obj("archive") ?: return null
    val author = modules.obj("module_author")
    val stat = archive.obj("stat")
    return VideoSummary(
      bvid = archive.string("bvid"),
      title = archive.string("title"),
      pic = fixPicUrl(archive.string("cover")),
      ownerName = author?.string("name").orEmpty(),
      ownerFace = fixPicUrl(author?.string("face").orEmpty()),
      ownerMid = author?.long("mid") ?: 0L,
      view = BiliNumberParser.toLong(stat?.get("play") ?: stat?.get("view")),
      danmaku = BiliNumberParser.toInt(stat?.get("danmaku")),
      duration = BiliNumberParser.parseDuration(archive["duration_text"]),
      pubdate = author?.long("pub_ts") ?: 0L,
      badge = filterBadge(archive.obj("badge")?.string("text").orEmpty()),
    )
  }

  fun fromHistory(json: JsonObject): VideoSummary {
    val history = json.obj("history")
    val cover = json.string("cover").ifBlank { json.string("pic") }
    val badge = json.string("badge")
    val business = history?.string("business").orEmpty()
    val uri = json.pgcUri(history)
    val pgcEpisodeId = history.pgcEpisodeId(uri = uri).takeIf { it > 0L }
      ?: json.pgcEpisodeId(uri = uri).takeIf { it > 0L }
      ?: 0L
    val pgcSeasonId = history.pgcSeasonId(uri = uri).takeIf { it > 0L }
      ?: json.pgcSeasonId(uri = uri).takeIf { it > 0L }
      ?: 0L
    val pgcIndexShow = json.string("show_title")
      .ifBlank { json.string("long_title") }
      .ifBlank { json.string("subtitle") }
      .ifBlank { history?.string("part").orEmpty() }
    val pgcEpisodeIndex = if (pgcEpisodeId > 0L || pgcSeasonId > 0L) {
      json.pgcEpisodeIndex(history)
    } else {
      0
    }
    val isLive = json.int("live_status") == 1 ||
      business == "live" ||
      badge.contains("\u76f4\u64ad") ||
      badge == "\u672a\u5f00\u64ad"

    return VideoSummary(
      bvid = history?.string("bvid").orEmpty(),
      title = json.string("title"),
      pic = fixPicUrl(cover),
      ownerName = json.string("author_name"),
      ownerFace = fixPicUrl(json.string("author_face")),
      ownerMid = json.long("author_mid"),
      view = BiliNumberParser.toLong(json.obj("stat")?.get("view")),
      danmaku = BiliNumberParser.toInt(json.obj("stat")?.get("danmaku")),
      duration = BiliNumberParser.parseDuration(json["duration"]),
      pubdate = json.long("pubdate"),
      badge = filterBadge(badge),
      progress = json.int("progress"),
      viewAt = json.long("view_at"),
      cid = history?.long("cid")?.takeIf { it != 0L } ?: (history?.long("oid") ?: 0L),
      historyPage = (history?.int("page") ?: 0).takeIf { it > 0 } ?: pgcEpisodeIndex,
      historyPart = history?.string("part").orEmpty()
        .ifBlank { json.string("long_title") }
        .ifBlank { json.string("show_title") },
      historyVideos = json.int("videos"),
      isLive = isLive,
      pgcSeasonId = pgcSeasonId,
      pgcEpisodeId = pgcEpisodeId,
      pgcTypeName = json.string("season_type_name")
        .ifBlank { history?.string("season_type_name").orEmpty() },
      pgcIndexShow = pgcIndexShow,
      pgcEpisodeIndex = pgcEpisodeIndex,
    )
  }

  fun fromSearch(json: JsonObject): VideoSummary {
    return VideoSummary(
      bvid = json.string("bvid"),
      title = stripHtmlTags(json.string("title")),
      pic = fixPicUrl(json.string("pic")),
      ownerName = json.string("author"),
      ownerFace = fixPicUrl(json.searchOwnerFace()),
      ownerMid = json.long("mid"),
      view = BiliNumberParser.toLong(json["play"]),
      danmaku = BiliNumberParser.toInt(json["danmaku"]),
      duration = BiliNumberParser.parseDuration(json["duration"]),
      pubdate = json.long("pubdate"),
      badge = filterBadge(json.string("badge")),
    )
  }

  fun fromPgcSearch(json: JsonObject): VideoSummary {
    val eps = json["eps"] as? kotlinx.serialization.json.JsonArray
    val firstEp = eps
      ?.firstOrNull()
      ?.asObjectOrNull()
    return VideoSummary(
      bvid = "",
      title = stripHtmlTags(json.string("title")),
      pic = fixPicUrl(json.string("cover")),
      ownerName = json.string("season_type_name"),
      ownerFace = fixPicUrl(json.string("cover")),
      ownerMid = 0L,
      view = BiliNumberParser.parseCountText(
        json.string("subtitle")
          .ifBlank { json.string("desc") }
          .ifBlank { json.string("evaluate") },
      ),
      danmaku = BiliNumberParser.toInt(
        json["danmaku"]
          ?: json["danmaku_count"]
          ?: json["dm"],
      ),
      duration = 0,
      pubdate = json.long("pub_time").takeIf { it > 0L } ?: json.long("pubtime"),
      badge = filterBadge(
        json.string("index_show")
          .ifBlank { json.string("season_type_name") },
      ),
      pgcSeasonId = json.long("season_id").takeIf { it > 0L } ?: json.long("pgc_season_id"),
      pgcEpisodeId = firstEp?.long("id") ?: 0L,
      pgcTypeName = json.string("season_type_name"),
      pgcIndexShow = json.string("index_show"),
    )
  }

  fun fromPgcSeasonIndex(json: JsonObject): VideoSummary {
    val firstEp = json.obj("first_ep")
    return VideoSummary(
      bvid = "",
      title = stripHtmlTags(json.string("title")),
      pic = fixPicUrl(json.string("cover")),
      ownerName = json.string("season_type_name"),
      ownerFace = fixPicUrl(json.string("cover")),
      ownerMid = 0L,
      view = 0L,
      danmaku = 0,
      duration = 0,
      pubdate = 0L,
      badge = filterBadge(
        json.string("index_show")
          .ifBlank { json.string("badge") },
      ),
      pgcSeasonId = json.long("season_id"),
      pgcEpisodeId = firstEp?.long("ep_id") ?: firstEp?.long("id") ?: 0L,
      pgcTypeName = json.string("season_type_name"),
      pgcIndexShow = json.string("index_show"),
    )
  }

  fun fromSpace(json: JsonObject): VideoSummary {
    return VideoSummary(
      bvid = json.string("bvid"),
      title = json.string("title"),
      pic = fixPicUrl(json.string("pic")),
      ownerName = json.string("author"),
      ownerFace = "",
      ownerMid = json.long("mid"),
      view = BiliNumberParser.toLong(json["play"]),
      danmaku = BiliNumberParser.toInt(json["video_review"]),
      duration = BiliNumberParser.parseDuration(json["length"]),
      pubdate = json.long("created"),
      badge = filterBadge(json.string("badge")),
    )
  }

  private fun JsonObject.searchOwnerFace(): String {
    return string("upic")
      .ifBlank { string("face") }
      .ifBlank { string("avatar") }
      .ifBlank { obj("owner")?.string("face").orEmpty() }
  }

  fun fixPicUrl(url: String): String {
    return when {
      url.startsWith("//") -> "https:$url"
      url.startsWith("http://") -> "https://${url.removePrefix("http://")}"
      else -> url
    }
  }

  private fun stripHtmlTags(text: String): String {
    return text.replace(HtmlTagRegex, "")
  }

  private fun filterBadge(badge: String): String {
    return if (badge == "\u6295\u7a3f\u89c6\u9891" || badge == "\u6295\u7a3f") "" else badge
  }

  private fun JsonObject?.pgcEpisodeId(uri: String): Long {
    if (this == null) {
      return uri.extractLongAfter("ep")
    }
    return long("epid").takeIf { it > 0L }
      ?: long("ep_id").takeIf { it > 0L }
      ?: long("episode_id").takeIf { it > 0L }
      ?: uri.extractLongAfter("ep")
  }

  private fun JsonObject?.pgcSeasonId(uri: String): Long {
    if (this == null) {
      return uri.extractLongAfter("ss")
    }
    return long("season_id").takeIf { it > 0L }
      ?: long("ssid").takeIf { it > 0L }
      ?: long("pgc_season_id").takeIf { it > 0L }
      ?: uri.extractLongAfter("ss")
  }

  private fun String.extractLongAfter(prefix: String): Long {
    return Regex("""(?:^|[/?&#])$prefix(\d+)""")
      .find(this)
      ?.groupValues
      ?.getOrNull(1)
      ?.toLongOrNull()
      ?: 0L
  }

  private fun JsonObject.pgcEpisodeIndex(history: JsonObject?): Int {
    return listOf(
      history?.int("page") ?: 0,
      history?.int("ep_index") ?: 0,
      history?.int("episode_index") ?: 0,
      history?.int("index") ?: 0,
      int("page"),
      int("ep_index"),
      int("episode_index"),
      int("index"),
    ).firstOrNull { it > 0 }
      ?: parseEpisodeIndex(
        history?.string("part").orEmpty(),
        string("index_show"),
        string("show_title"),
        string("long_title"),
      )
  }

  private fun JsonObject.pgcUri(history: JsonObject?): String {
    return string("uri")
      .ifBlank { string("redirect_url") }
      .ifBlank { string("show_link") }
      .ifBlank { string("link") }
      .ifBlank { history?.string("uri").orEmpty() }
      .ifBlank { history?.string("redirect_url").orEmpty() }
      .ifBlank { history?.string("show_link").orEmpty() }
      .ifBlank { history?.string("link").orEmpty() }
  }

  private fun parseEpisodeIndex(vararg texts: String): Int {
    return texts
      .asSequence()
      .mapNotNull { text -> EpisodeIndexRegex.find(text)?.groupValues?.getOrNull(1)?.toIntOrNull() }
      .firstOrNull { it > 0 }
      ?: 0
  }

  private val HtmlTagRegex = Regex("<[^>]*>")
  private val EpisodeIndexRegex = Regex("""(?:第|^|EP\.?\s*)(\d+)(?:[集话話]|$|\s)""", RegexOption.IGNORE_CASE)
}
