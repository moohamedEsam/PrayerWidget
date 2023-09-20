package com.example.prayerwidget.domain.usecase.settings

import com.example.prayerwidget.data.datastore.Settings

fun interface UpdateSettingsUseCase : suspend ((Settings) -> Settings) -> Unit