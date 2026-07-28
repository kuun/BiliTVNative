package com.kirin.bilitv.ui.player

import com.kirin.bilitv.core.model.VideoSummary
import com.kirin.bilitv.core.player.PlaybackRequest
import com.kirin.bilitv.core.player.PlaybackVideoMetadata

internal data class PlayerNextEpisodeCompletion(
  val request: PlaybackRequest,
  val title: String,
)

internal fun PlaybackRequest.nextEpisodeCompletion(
  metadata: PlaybackVideoMetadata?,
  selectedQualityId: Int?,
): PlayerNextEpisodeCompletion? {
  val pages = metadata?.pages.orEmpty()
  val currentIndex = pages.indexOfFirst { episode ->
    (pgcEpisodeId > 0L && episode.pgcEpisodeId == pgcEpisodeId) ||
      episode.cid == cid ||
      (historyPage > 0 && episode.page == historyPage)
  }
  val nextEpisode = pages.getOrNull(currentIndex + 1) ?: return null
  val nextRequest = copy(
    bvid = nextEpisode.bvid.ifBlank { bvid },
    cid = nextEpisode.cid,
    aid = nextEpisode.aid.takeIf { it > 0L } ?: aid,
    startPositionMs = 0L,
    preferredQualityId = selectedQualityId,
    forceStartPosition = true,
    historyPage = nextEpisode.page,
    advanceToNextHistoryEpisode = false,
    pgcEpisodeId = nextEpisode.pgcEpisodeId.takeIf { it > 0L } ?: pgcEpisodeId,
  )
  return PlayerNextEpisodeCompletion(
    request = nextRequest,
    title = nextEpisode.title.ifBlank { nextRequest.title },
  )
}

internal fun List<VideoSummary>.firstCompletionRelatedVideo(currentBvid: String): VideoSummary? {
  return firstOrNull { video -> !video.bvid.equals(currentBvid, ignoreCase = true) }
}
