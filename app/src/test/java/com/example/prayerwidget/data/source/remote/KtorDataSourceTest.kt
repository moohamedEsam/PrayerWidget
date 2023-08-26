package com.example.prayerwidget.data.source.remote

import com.example.prayerwidget.presentation.di.MainModule
import io.kotest.matchers.shouldNotBe
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.Calendar


class KtorDataSourceTest {
    private val client: HttpClient = MainModule().provideKtorClient()
    private val dataSource = KtorDataSource(client)

    @Test
    fun getPrayer() = runTest {
        val calendar = Calendar.getInstance()
        val result =
            dataSource.getPrayer(calendar[Calendar.YEAR], calendar[Calendar.MONTH], "EG", "Banha")
        result.onRight { it.size shouldNotBe 0 }
            .onLeft { Assert.fail() }
    }
}