package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Timings(
    @SerialName("Asr")
    val asr: String,
    @SerialName("Dhuhr")
    val dhuhr: String,
    @SerialName("Fajr")
    val fajr: String,
    @SerialName("Imsak")
    val imsak: String,
    @SerialName("Isha")
    val isha: String,
    @SerialName("Maghrib")
    val maghrib: String,
    @SerialName("Midnight")
    val midnight: String,
    @SerialName("Sunrise")
    val sunrise: String,
    @SerialName("Sunset")
    val sunset: String
)