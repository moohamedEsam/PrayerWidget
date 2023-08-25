package com.example.prayerwidget.data.model


import com.example.prayerwidget.domain.model.Date
import com.example.prayerwidget.domain.model.Prayer
import com.example.prayerwidget.domain.model.Timings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPrayer(
    @SerialName("date")
    val date: Date,
    @SerialName("timings")
    val timings: Timings
)

fun NetworkPrayer.toPrayer(city: String) = Prayer(
    asr = timings.asr,
    dhuhr = timings.dhuhr,
    fajr = timings.fajr,
    imsak = timings.imsak,
    isha = timings.isha,
    maghrib = timings.maghrib,
    midnight = timings.midnight,
    sunrise = timings.sunrise,
    sunset = timings.sunset,
    date = date.timestamp.toLong(),
    city = city
)