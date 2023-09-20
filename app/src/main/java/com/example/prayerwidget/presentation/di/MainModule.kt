package com.example.prayerwidget.presentation.di

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.source.PrayerRepository
import com.example.prayerwidget.data.source.local.PrayerDatabase
import com.example.prayerwidget.data.work.PrayerWorker
import com.example.prayerwidget.domain.usecase.GetCurrentPrayerUseCase
import com.example.prayerwidget.domain.usecase.ObserveSettingsUseCase
import com.example.prayerwidget.domain.usecase.PRAYER_INTENT_ACTION
import com.example.prayerwidget.domain.usecase.SetAlarmUseCase
import com.example.prayerwidget.domain.usecase.SyncUseCase
import com.example.prayerwidget.domain.usecase.getCurrentDay
import com.example.prayerwidget.domain.usecase.settings.UpdateSettingsUseCase
import com.example.prayerwidget.domain.usecase.settings.updatePrayerAlarmEnabledUseCase
import com.example.prayerwidget.presentation.broadcasts.AlarmReceiver
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope
import java.util.Calendar

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
    fun provideSyncUseCase() = SyncUseCase {
        val workRequest = OneTimeWorkRequestBuilder<PrayerWorker>()
            .setConstraints(PrayerWorker.constraints())
            .addTag(PrayerWorker.TAG)
            .build()
        WorkManager.getInstance(androidContext()).enqueue(workRequest)
    }

    context (Scope)
    @OptIn(ExperimentalCoroutinesApi::class)
    @Factory
    fun provideGetCurrentPrayer(prayerRepository: PrayerRepository) = GetCurrentPrayerUseCase {
        val today = getCurrentDay()
        androidContext().dataStore.data.map { it.city }
            .flatMapLatest { city ->
                prayerRepository.getPrayerByDateFlow(today.year, today.month, today.day, city)
            }
    }

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

        val calendar = Calendar.getInstance().apply {
            add(Calendar.SECOND, 5)
        }
        val alarmManager = androidContext().getSystemService(AlarmManager::class.java)
        alarmManager.setExact(
            /* type = */ AlarmManager.RTC,
            /* triggerAtMillis = */ calendar.timeInMillis,
            /* operation = */ pendingIntent
        )
    }

    context(Scope)
    @Factory
    fun provideSetAlarmUseCase() = SetAlarmUseCase {
        oneTimeAlarm()
//        val context = androidContext()
//        val intent = Intent(context, AlarmReceiver::class.java).apply { action = PRAYER_INTENT_ACTION }
//        val isActive = checkIfAlarmIsActive(context, intent)
//        if (isActive) return@SetAlarmUseCase
//        val alarmManager = context.getSystemService(AlarmManager::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            /* context = */ context,
//            /* requestCode = */ 0,
//            /* intent = */ intent,
//            /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val calendar = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, 1)
//            set(Calendar.MINUTE, 0)
//        }
//        alarmManager.setInexactRepeating(
//            /* type = */ AlarmManager.RTC,
//            /* triggerAtMillis = */ calendar.timeInMillis,
//            /* intervalMillis = */ AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//            /* operation = */ pendingIntent
//        )
    }

    @Factory
    fun providePrayerDao(database: PrayerDatabase) = database.prayerDao()
}

fun checkIfAlarmIsActive(context: Context, intent: Intent) = PendingIntent.getBroadcast(
    context,
    1,
    intent,
    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
) != null
