package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Gregorian(
    @SerialName("date")
    val date: String,
    @SerialName("day")
    val day: String,
    @SerialName("designation")
    val designation: Designation,
    @SerialName("format")
    val format: String,
    @SerialName("month")
    val month: Month,
    @SerialName("weekday")
    val weekday: Weekday,
    @SerialName("year")
    val year: String
)