package com.example.prayerwidget.data.source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.prayerwidget.data.source.local.PrayerDatabase
import com.example.prayerwidget.data.source.remote.KtorDataSource
import com.example.prayerwidget.presentation.di.MainModule
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class PrayerRepositoryTest {
    private lateinit var prayerRepository: PrayerRepository
    private lateinit var prayerDatabase: PrayerDatabase

    @Before
    fun setUp() {
        val client = MainModule().provideKtorClient()
        val ktorDataSource = KtorDataSource(client)

        prayerDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PrayerDatabase::class.java
        ).allowMainThreadQueries().build()

        val prayerDao = prayerDatabase.prayerDao()
        prayerRepository = OfflinePrayerRepository(prayerDao, ktorDataSource)
    }

    @Test
    fun testSync() = runTest {
        val localDate = LocalDate.now()
        val result = prayerRepository.sync(localDate.year, localDate.monthValue, "EG", "Banha")
        result.onFailure { Assert.fail() }

        val prayer = prayerRepository.getPrayerByDate(
            year = localDate.year,
            month = localDate.monthValue,
            day = localDate.dayOfMonth,
            city = "Banha"
        )
        prayer shouldNotBe null
    }

    @After
    fun tearDown() {
        prayerDatabase.close()
    }
}