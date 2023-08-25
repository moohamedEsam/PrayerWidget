package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hijri(
    @SerialName("date")
    val date: String,
    @SerialName("day")
    val day: String,
    @SerialName("designation")
    val designation: Designation,
    @SerialName("format")
    val format: String,
    @SerialName("holidays")
    val holidays: List<String>,
    @SerialName("month")
    val month: MonthX,
    @SerialName("weekday")
    val weekday: WeekdayX,
    @SerialName("year")
    val year: String
)