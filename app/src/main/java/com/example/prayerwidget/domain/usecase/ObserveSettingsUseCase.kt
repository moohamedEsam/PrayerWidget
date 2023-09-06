package com.example.prayerwidget.domain.usecase

import com.example.prayerwidget.data.datastore.Settings
import kotlinx.coroutines.flow.Flow

fun interface ObserveSettingsUseCase : () -> Flow<Settings>