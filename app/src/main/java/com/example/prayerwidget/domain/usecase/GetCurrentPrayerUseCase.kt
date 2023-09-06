package com.example.prayerwidget.domain.usecase

import com.example.prayerwidget.domain.model.Prayer
import kotlinx.coroutines.flow.Flow

fun interface GetCurrentPrayerUseCase : () -> Flow<Prayer>