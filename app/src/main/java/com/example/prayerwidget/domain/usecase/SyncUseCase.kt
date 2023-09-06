package com.example.prayerwidget.domain.usecase

import androidx.work.Operation

fun interface SyncUseCase : suspend () -> Operation