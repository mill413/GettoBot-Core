package top.harumill.getto.tools

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatter(pattern: String): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

fun getCurrentDateTime(): String = formatter("yyyy-MM-dd HH:mm:ss.SSSS").format(LocalDateTime.now())

fun getCurrentDate(): String = formatter("yyyy-MM-dd").format(LocalDateTime.now())

fun timeStampToDateTime(format: String = "yyyy-MM-dd HH:mm:ss", timestamp: Long): String {
    val dt = Instant.ofEpochSecond(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    return formatter(format).format(dt)
}