package com.example.prayerwidget

import android.app.Application
import android.app.NotificationManager
import com.example.prayerwidget.data.work.workerModule
import com.example.prayerwidget.domain.usecase.SetAlarmUseCase
import com.example.prayerwidget.presentation.di.MainModule
import com.example.prayerwidget.presentation.services.createCallToPrayerChannel
import org.koin.android.ext.android.inject
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
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createCallToPrayerChannel()

        val setAlarmUseCase by inject<SetAlarmUseCase>()
        setAlarmUseCase()
    }
}