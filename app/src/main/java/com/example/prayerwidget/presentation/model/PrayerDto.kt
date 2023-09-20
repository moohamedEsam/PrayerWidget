package com.example.prayerwidget.presentation.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.prayerwidget.domain.model.Prayer

data class PrayerDto(
    val prayers: SnapshotStateList<SinglePrayer> = mutableStateListOf(),
    val day: Int,
    val month: Int,
    val year: Int,
    val city: String
) {
    companion object
}

fun Prayer.toPrayerDto() = PrayerDto(
    prayers = mutableStateListOf(
        SinglePrayer("الفجر", fajr),
        SinglePrayer("الظهر", dhuhr),
        SinglePrayer("العصر", asr),
        SinglePrayer("المغرب", maghrib),
        SinglePrayer("العشاء", isha),
    ),
    day = day,
    month = month,
    year = year,
    city = city
)

fun PrayerDto.Companion.empty() = PrayerDto(
    prayers = mutableStateListOf(),
    day = 21,
    month = 12,
    year = 2023,
    city = "Carlyn"
)