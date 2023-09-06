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
    asr = "",
    dhuhr = "",
    fajr = "",
    imsak = "",
    isha = "",
    maghrib = "",
    midnight = "",
    sunrise = "",
    sunset = "",
    day = 0,
    month = 0,
    year = 0,
    city = ""
)