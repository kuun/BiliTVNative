package com.kirin.bilitv.core.network

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

object BiliNumberParser {
  fun parseCountText(text: String): Long {
    val match = CountTextRegex.find(text) ?: return 0L
    val value = match.groupValues.getOrNull(1)?.toDoubleOrNull() ?: return 0L
    val unit = match.groupValues.getOrNull(2).orEmpty()
    val multiplier = when (unit) {
      "\u4e07" -> 10_000.0
      "\u4ebf" -> 100_000_000.0
      else -> 1.0
    }
    return (value * multiplier).toLong()
  }

  fun toLong(value: JsonElement?): Long {
    val primitive = value as? JsonPrimitive ?: return 0L
    primitive.longOrNull?.let { return it }

    val text = primitive.contentOrNull.orEmpty()
    text.toLongOrNull()?.let { return it }

    return when {
      text.endsWith("\u4e07") -> ((text.dropLast(1).toDoubleOrNull() ?: 0.0) * 10_000).toLong()
      text.endsWith("\u4ebf") -> ((text.dropLast(1).toDoubleOrNull() ?: 0.0) * 100_000_000).toLong()
      else -> 0L
    }
  }

  fun toInt(value: JsonElement?): Int {
    val primitive = value as? JsonPrimitive ?: return 0
    primitive.intOrNull?.let { return it }

    val text = primitive.contentOrNull.orEmpty()
    text.toIntOrNull()?.let { return it }

    return when {
      text.endsWith("\u4e07") -> ((text.dropLast(1).toDoubleOrNull() ?: 0.0) * 10_000).toInt()
      text.endsWith("\u4ebf") -> ((text.dropLast(1).toDoubleOrNull() ?: 0.0) * 100_000_000).toInt()
      else -> 0
    }
  }

  fun parseDuration(value: JsonElement?): Int {
    val primitive = value as? JsonPrimitive ?: return 0
    primitive.intOrNull?.let { return it }

    val parts = primitive.contentOrNull.orEmpty().split(":")
    return when (parts.size) {
      2 -> (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0)
      3 -> (parts[0].toIntOrNull() ?: 0) * 3600 +
        (parts[1].toIntOrNull() ?: 0) * 60 +
        (parts[2].toIntOrNull() ?: 0)
      else -> 0
    }
  }

  private val CountTextRegex = Regex("""(\d+(?:\.\d+)?)([万亿]?)""")
}
