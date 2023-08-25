package com.example.prayerwidget.data.source

import com.example.prayerwidget.data.model.PrayerEntity
import com.example.prayerwidget.domain.model.Prayer
import kotlinx.coroutines.flow.Flow

interface PrayerRepository {
    fun getPrayerByDate(year: Int, month: Int, day: Int, city: String): Flow<List<Prayer>>

    suspend fun insertPrayers(prayers: List<PrayerEntity>)

    suspend fun sync(year: Int, month: Int, country: String, city: String): Result<Unit>
}