package com.example.prayerwidget.domain.usecase

import com.example.prayerwidget.data.datastore.Settings

fun interface UpdateSettingsUseCase : suspend ((Settings) -> Settings) -> Unit