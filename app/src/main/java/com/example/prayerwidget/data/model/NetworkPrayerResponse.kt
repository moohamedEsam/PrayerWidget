package com.example.prayerwidget.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkPrayerResponse(
    val code: Int,
    val status: String,
    val data: List<NetworkPrayer>
)
