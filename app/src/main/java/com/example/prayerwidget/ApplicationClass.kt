package com.example.prayerwidget

import android.app.Application
import com.example.prayerwidget.data.work.workerModule
import com.example.prayerwidget.presentation.di.MainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationClass)
            workManagerFactory()
            modules(MainModule().module, workerModule)
            androidLogger()
        }
    }
}