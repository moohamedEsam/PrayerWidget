package com.example.prayerwidget.domain

import com.example.prayerwidget.presentation.model.SinglePrayer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun calculateTimeLeft(
    singlePrayer: SinglePrayer
): Duration {
    val currentTime = System.currentTimeMillis()
    val (hour, minute) = singlePrayer.time
        .dropLast(7)
        .split(":")
        .map { it.toIntOrNull() ?: 0 }
    val localDateTime = LocalDate
        .now()
        .atStartOfDay()
        .withHour(hour)
        .withMinute(minute)

    var result = (localDateTime.timeInMillis() - currentTime)
        .toDuration(DurationUnit.MILLISECONDS)
    result = if (result.isNegative())
        result + (1).toDuration(DurationUnit.DAYS)
    else
        result

    return result
}

fun LocalDateTime.timeInMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
fun LocalDate.timeInMillis() = this.atStartOfDay().timeInMillis()