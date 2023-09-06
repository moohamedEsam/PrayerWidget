package com.example.prayerwidget.presentation.model

import com.example.prayerwidget.domain.model.Prayer
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class PrayerDto(
    val asr: SinglePrayer,
    val dhuhr: SinglePrayer,
    val fajr: SinglePrayer,
    val isha: SinglePrayer,
    val maghrib: SinglePrayer,
    val day: Int,
    val month: Int,
    val year: Int,
    val city: String
) {
    companion object
}

fun Prayer.toPrayerDto() = PrayerDto(
    asr = SinglePrayer("العصر", asr),
    dhuhr = SinglePrayer("الظهر", dhuhr),
    fajr = SinglePrayer("الفجر", fajr),
    isha = SinglePrayer("العشاء", isha),
    maghrib = SinglePrayer("المغرب", maghrib),
    day = day,
    month = month,
    year = year,
    city = city
)

fun PrayerDto.Companion.empty() = PrayerDto(
    asr = SinglePrayer(
        name = "asr",
        time = "16:02 (EEST)",
        alarmEnabled = true,
        timeLeft = (0).toDuration(DurationUnit.HOURS)
    ),
    dhuhr = SinglePrayer(
        name = "duhr",
        time = "12:00 (EEST)",
        alarmEnabled = true,
        timeLeft = (0).toDuration(DurationUnit.HOURS)
    ), fajr = SinglePrayer(
        name = "fajr",
        time = "04:05 (EEST)",
        alarmEnabled = true,
        timeLeft = (0).toDuration(DurationUnit.HOURS)
    ), isha = SinglePrayer(
        name = "isha",
        time = "20:20 (EEST)",
        alarmEnabled = true,
        timeLeft = (0).toDuration(DurationUnit.HOURS)
    ), maghrib = SinglePrayer(
        name = "maghrib",
        time = "17:05 (EEST)",
        alarmEnabled = false,
        timeLeft = (0).toDuration(DurationUnit.HOURS)
    ), day = 21, month = 12, year = 2023, city = "Carlyn"
)