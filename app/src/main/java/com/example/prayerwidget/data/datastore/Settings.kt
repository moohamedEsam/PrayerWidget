package com.example.prayerwidget.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val city: String = "",
    val country: String = "",
    val alarms: List<Boolean> = List(5) { false },
)