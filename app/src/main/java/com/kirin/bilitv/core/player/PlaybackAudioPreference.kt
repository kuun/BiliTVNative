package com.kirin.bilitv.core.player

enum class PlaybackAudioPreference(val key: String) {
  Highest("highest"),
  HiRes("hi_res"),
  Dolby("dolby"),
  StandardHighest("standard_highest");

  companion object {
    fun fromKey(key: String?): PlaybackAudioPreference {
      return entries.firstOrNull { preference -> preference.key == key } ?: Highest
    }
  }
}
