package com.example.prayerwidget

import android.app.Application
import com.example.prayerwidget.presentation.di.MainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationClass)
            modules(MainModule().module)
            androidLogger()
        }
    }
}