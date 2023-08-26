package com.example.prayerwidget.presentation.di

import com.example.prayerwidget.data.source.local.PrayerDatabase
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

    @Factory
    fun providePrayerDao(database: PrayerDatabase) = database.prayerDao()
}

