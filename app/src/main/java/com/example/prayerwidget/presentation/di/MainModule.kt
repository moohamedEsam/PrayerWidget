package com.example.prayerwidget.presentation.di

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.source.PrayerRepository
import com.example.prayerwidget.data.source.local.PrayerDatabase
import com.example.prayerwidget.data.work.PrayerWorker
import com.example.prayerwidget.domain.usecase.GetCurrentPrayerUseCase
import com.example.prayerwidget.domain.usecase.ObserveSettingsUseCase
import com.example.prayerwidget.domain.usecase.SyncUseCase
import com.example.prayerwidget.domain.usecase.UpdateSettingsUseCase
import com.example.prayerwidget.domain.usecase.getCurrentDay
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
import java.util.concurrent.TimeUnit

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
        val workRequest = PeriodicWorkRequestBuilder<PrayerWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
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

    @Factory
    fun providePrayerDao(database: PrayerDatabase) = database.prayerDao()
}

