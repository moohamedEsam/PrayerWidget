package com.example.prayerwidget.domain.usecase

import android.os.Build
import java.time.LocalDate
import java.util.Calendar

fun getCurrentDay(): TodayDate {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val today = LocalDate.now()
        TodayDate(year = today.year, month = today.monthValue, day = today.dayOfMonth)
    } else {
        val today = Calendar.getInstance()
        TodayDate(
            year = today[Calendar.YEAR],
            month = today[Calendar.MONTH],
            day = today[Calendar.DAY_OF_MONTH]
        )
    }
}

fun getCurrentDayInMillis() = Calendar.getInstance().apply {
    set(Calendar.MINUTE, 0)
    set(Calendar.HOUR, 0)
    set(Calendar.SECOND, 0)
}.timeInMillis


data class TodayDate(
    val year: Int,
    val month: Int,
    val day: Int
)