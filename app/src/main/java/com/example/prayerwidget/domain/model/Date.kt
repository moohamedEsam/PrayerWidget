package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Date(
    @SerialName("gregorian")
    val gregorian: Gregorian,
    @SerialName("hijri")
    val hijri: Hijri,
    @SerialName("readable")
    val readable: String,
    @SerialName("timestamp")
    val timestamp: String
)