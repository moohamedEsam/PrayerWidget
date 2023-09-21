package com.example.prayerwidget.domain.usecase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.prayerwidget.data.work.PrayerWorker

fun interface SyncUseCase : suspend () -> Unit


fun syncUseCase(context: Context) = SyncUseCase {
    val workRequest = OneTimeWorkRequestBuilder<PrayerWorker>()
        .setConstraints(PrayerWorker.constraints())
        .addTag(PrayerWorker.TAG)
        .build()
    WorkManager.getInstance(context).enqueue(workRequest)
}