package com.example.prayerwidget.domain.usecase

import com.example.prayerwidget.presentation.model.SinglePrayer
import java.util.Calendar
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun calculateTimeLeft(
    singlePrayer: SinglePrayer
): Duration {
//    val currentDay = getCurrentDayInMillis()
    val currentTime = System.currentTimeMillis()
    val (hour, minute) = singlePrayer.time
        .dropLast(7)
        .split(":")
        .map { it.toIntOrNull() ?: 0 }
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }
    var result = (calendar.timeInMillis - currentTime)
        .toDuration(DurationUnit.MILLISECONDS)
    result = if (result.isNegative())
        result + (1).toDuration(DurationUnit.DAYS)
    else
        result

    return result
}