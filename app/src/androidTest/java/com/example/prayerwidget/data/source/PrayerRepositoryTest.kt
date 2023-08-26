package com.example.prayerwidget.data.source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.prayerwidget.data.source.local.PrayerDatabase
import com.example.prayerwidget.data.source.remote.KtorDataSource
import com.example.prayerwidget.presentation.di.MainModule
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Calendar

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
        val calendar = Calendar.getInstance()
        val result =
            prayerRepository.sync(calendar[Calendar.YEAR], calendar[Calendar.MONTH], "EG", "Banha")
        result.onFailure { Assert.fail() }

        val prayers = prayerRepository.getPrayerByDate(
            year = calendar[Calendar.YEAR],
            month = calendar[Calendar.MONTH],
            day = calendar[Calendar.DAY_OF_MONTH],
            city = "Banha"
        )
        prayers.test {
            val items = awaitItem()
            items.size shouldNotBe 0
        }
    }

    @After
    fun tearDown() {
        prayerDatabase.close()
    }
}