package top.harumill.getto.tools

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
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

fun dateTimeToTimeStamp(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Long =
    LocalDateTime.of(year, month, day, hour, minute, second).toEpochSecond(ZoneOffset.of("+8"))
