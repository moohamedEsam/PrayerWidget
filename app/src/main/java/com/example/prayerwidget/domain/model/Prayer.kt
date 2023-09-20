package com.example.prayerwidget.domain.model

data class Prayer(
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
) {
    companion object
}

fun Prayer.Companion.empty() = Prayer(
    asr = "15:15 (EEST)",
    dhuhr = "15:15 (EEST)",
    fajr = "15:15 (EEST)",
    imsak = "15:15 (EEST)",
    isha = "15:15 (EEST)",
    maghrib = "15:15 (EEST)",
    midnight = "15:15 (EEST)",
    sunrise = "15:15 (EEST)",
    sunset = "15:15 (EEST)",
    day = 0,
    month = 0,
    year = 0,
    city = ""
)