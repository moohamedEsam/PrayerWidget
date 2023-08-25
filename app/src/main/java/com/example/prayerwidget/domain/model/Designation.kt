package com.example.prayerwidget.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Designation(
    @SerialName("abbreviated")
    val abbreviated: String,
    @SerialName("expanded")
    val expanded: String
)