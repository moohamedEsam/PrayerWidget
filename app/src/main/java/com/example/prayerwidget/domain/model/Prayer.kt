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
    val date: Long,
    val city: String
)