package com.example.prayerwidget.data.model

import androidx.room.Entity
import com.example.prayerwidget.domain.model.Prayer

@Entity(tableName = "prayerEntity", primaryKeys = ["city", "day", "month", "year"])
data class PrayerEntity(
    val asr: String,
    val dhuhr: String,
    val fajr: String,
    val imsak: String,
    val isha: String,
    val maghrib: String,
    val midnight: String,
    val sunrise: String,
    val sunset: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val city: String
)

fun PrayerEntity.toPrayer() = Prayer(
    asr = asr,
    dhuhr = dhuhr,
    fajr = fajr,
    imsak = imsak,
    isha = isha,
    maghrib = maghrib,
    midnight = midnight,
    sunrise = sunrise,
    sunset = sunset,
    city = city,
    day = day,
    month = month,
    year = year
)

fun Prayer.toEntity() = PrayerEntity(
    asr = asr,
    dhuhr = dhuhr,
    fajr = fajr,
    imsak = imsak,
    isha = isha,
    maghrib = maghrib,
    midnight = midnight,
    sunrise = sunrise,
    sunset = sunset,
    city = city,
    day = day,
    month = month,
    year = year
)