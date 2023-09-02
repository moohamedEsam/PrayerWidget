package com.example.prayerwidget.data.source

import com.example.prayerwidget.domain.model.Prayer
import kotlinx.coroutines.flow.Flow

interface PrayerRepository {
    fun getPrayerByDateFlow(year: Int, month: Int, day: Int, city: String): Flow<Prayer>

    suspend fun getPrayerByDate(year: Int, month: Int, day: Int, city: String): Prayer?

    suspend fun insertPrayers(prayers: List<Prayer>)

    suspend fun sync(year: Int, month: Int, country: String, city: String): Result<Unit>
}