package com.example.prayerwidget.data.work

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker {
        PrayerWorker(
            context = androidApplication(),
            workerParameters = get(),
            prayerRepository = get()
        )
    }
}