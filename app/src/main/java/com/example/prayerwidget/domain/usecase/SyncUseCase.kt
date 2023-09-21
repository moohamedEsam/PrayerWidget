package com.example.prayerwidget.domain.usecase

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.prayerwidget.data.work.PrayerWorker
import java.util.concurrent.TimeUnit

fun interface SyncUseCase : suspend () -> Unit


fun syncUseCase(context: Context) = SyncUseCase {
    val workRequest = PeriodicWorkRequestBuilder<PrayerWorker>(
        repeatInterval = 1,
        repeatIntervalTimeUnit = TimeUnit.DAYS
    ).setConstraints(PrayerWorker.constraints())
        .addTag(PrayerWorker.TAG)
        .build()
    WorkManager.getInstance(context).enqueue(workRequest)
}