package com.example.prayerwidget.data.source.remote

import arrow.core.raise.either
import arrow.core.raise.fold
import com.example.prayerwidget.data.model.NetworkPrayerResponse
import com.example.prayerwidget.data.model.toPrayer
import com.example.prayerwidget.domain.model.Prayer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Factory

@Factory
class KtorDataSource(
    private val client: HttpClient
) {
    suspend fun getPrayer(year: Int, month: Int, country: String, city: String) =
        either<Throwable, List<Prayer>> {
            val request = client.get("http://api.aladhan.com/v1/calendarByCity/$year/$month") {
                parameter("city", city)
                parameter("method", 5)
                parameter("country", country)
            }

            fold(
                block = { request.body() },
                recover = ::raise,
                transform = { response: NetworkPrayerResponse ->
                    response.data.map { it.toPrayer(city) }
                }
            )
        }
}