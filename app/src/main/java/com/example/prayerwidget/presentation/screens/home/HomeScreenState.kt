package com.example.prayerwidget.presentation.screens.home

import com.example.prayerwidget.presentation.model.PrayerDto

data class HomeScreenState(
    val city: String = "",
    val country: String = "",
    val prayer: PrayerDto? = null,
)
