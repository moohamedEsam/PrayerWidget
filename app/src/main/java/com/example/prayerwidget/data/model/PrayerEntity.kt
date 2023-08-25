package com.example.prayerwidget.data.model

import androidx.room.Entity
import com.example.prayerwidget.domain.model.Prayer

@Entity(tableName = "prayer_by_day", primaryKeys = ["date", "city"])
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
    val date: Long,
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
    date = date,
    city = city
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
    date = date,
    city = city
)