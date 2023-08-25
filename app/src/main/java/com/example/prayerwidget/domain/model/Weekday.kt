package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weekday(
    @SerialName("en")
    val en: String
)