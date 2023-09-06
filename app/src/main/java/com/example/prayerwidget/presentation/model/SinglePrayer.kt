package com.example.prayerwidget.presentation.model

import kotlin.time.Duration

data class SinglePrayer(
    val name: String = "",
    val time: String = "",
    val alarmEnabled: Boolean = false,
    val timeLeft: Duration = Duration.ZERO
)