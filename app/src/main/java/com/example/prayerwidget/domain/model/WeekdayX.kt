package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeekdayX(
    @SerialName("ar")
    val ar: String,
    @SerialName("en")
    val en: String
)