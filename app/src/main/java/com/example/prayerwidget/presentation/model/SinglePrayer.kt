package com.example.prayerwidget.presentation.model

import kotlin.time.Duration

data class SinglePrayer(
    val name: String = "",
    val time: String = "",
    var alarmEnabled: Boolean = false,
    var timeLeft: Duration = Duration.ZERO
)