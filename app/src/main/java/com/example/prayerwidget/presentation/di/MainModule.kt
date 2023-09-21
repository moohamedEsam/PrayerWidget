package com.example.prayerwidget.presentation.di

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.source.PrayerRepository
import com.example.prayerwidget.data.source.local.PrayerDatabase
import com.example.prayerwidget.domain.timeInMillis
import com.example.prayerwidget.domain.usecase.ObserveSettingsUseCase
import com.example.prayerwidget.domain.usecase.PRAYER_INTENT_ACTION
import com.example.prayerwidget.domain.usecase.getCurrentPrayerUseCase
import com.example.prayerwidget.domain.usecase.setAlarmUseCase
import com.example.prayerwidget.domain.usecase.settings.UpdateSettingsUseCase
import com.example.prayerwidget.domain.usecase.settings.updatePrayerAlarmEnabledUseCase
import com.example.prayerwidget.domain.usecase.syncUseCase
import com.example.prayerwidget.presentation.broadcasts.AlarmReceiver
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import java.time.LocalDateTime

@Module
@ComponentScan("com.example")
class MainModule {
    @Factory
    fun provideKtorClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = Logger.ANDROID
        }
    }

    context(Scope)
    @Single
    fun provideDatabase() = PrayerDatabase.createDatabase(androidContext())

    context(Scope)
    @Factory
    fun provideObserveSettingsUseCase() = ObserveSettingsUseCase {
        androidContext().dataStore.data
    }

    context(Scope)
    @Factory
    fun provideUpdateSettingsUseCase() = UpdateSettingsUseCase { callback ->
        androidContext().dataStore.updateData(callback)
    }

    context (Scope)
    @Factory
    fun provideSyncUseCase() = syncUseCase(androidContext())

    context (Scope)
    @Factory
    fun provideGetCurrentPrayer(prayerRepository: PrayerRepository) = getCurrentPrayerUseCase(
        context = androidContext(),
        prayerRepository = prayerRepository
    )

    context (Scope)
    @Factory
    fun provideUpdatePrayerAlarmEnabledUseCase() = updatePrayerAlarmEnabledUseCase(androidContext())

    context (Scope)
    @SuppressLint("ScheduleExactAlarm")
    fun oneTimeAlarm() {
        val intent = Intent(androidContext(), AlarmReceiver::class.java).apply {
            action = PRAYER_INTENT_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            /* context = */ androidContext(),
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val localDateTime = LocalDateTime.now().plusSeconds(5)
        val alarmManager = androidContext().getSystemService(AlarmManager::class.java)
        alarmManager.setExact(
            /* type = */ AlarmManager.RTC,
            /* triggerAtMillis = */ localDateTime.timeInMillis(),
            /* operation = */ pendingIntent
        )
    }

    context(Scope)
    @Factory
    fun provideSetAlarmUseCase() = setAlarmUseCase(androidContext())

    @Factory
    fun providePrayerDao(database: PrayerDatabase) = database.prayerDao()
}

fun checkIfAlarmIsActive(context: Context, intent: Intent) = PendingIntent.getBroadcast(
    context,
    1,
    intent,
    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
) != null
