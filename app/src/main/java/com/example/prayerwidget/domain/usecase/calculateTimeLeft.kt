package com.example.prayerwidget.domain.usecase

import com.example.prayerwidget.presentation.model.SinglePrayer
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun calculateTimeLeft(
    singlePrayer: SinglePrayer
): Duration {
    val currentDay = getCurrentDayInMillis()
    val currentTime = System.currentTimeMillis()
    val (hour, minute) = singlePrayer.time
        .dropLast(7)
        .split(":")
        .map { it.toIntOrNull() ?: 0 }
    val duration = hour.toDuration(DurationUnit.HOURS) + minute.toDuration(DurationUnit.MINUTES)
    val result = (currentDay + duration.toLong(DurationUnit.MILLISECONDS) - currentTime)
        .toDuration(DurationUnit.MILLISECONDS)
    return if (result.inWholeSeconds < 0)
        Duration.ZERO
    else
        result
}