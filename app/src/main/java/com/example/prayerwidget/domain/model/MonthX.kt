package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthX(
    @SerialName("ar")
    val ar: String,
    @SerialName("en")
    val en: String,
    @SerialName("number")
    val number: Int
)