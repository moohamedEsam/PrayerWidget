package com.example.prayerwidget.data.source

import com.example.prayerwidget.data.model.PrayerEntity
import com.example.prayerwidget.data.model.toEntity
import com.example.prayerwidget.data.model.toPrayer
import com.example.prayerwidget.data.source.local.PrayerDao
import com.example.prayerwidget.data.source.remote.KtorDataSource
import com.example.prayerwidget.domain.model.Prayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory


@Factory
class OfflinePrayerRepository(
    private val prayerDao: PrayerDao,
    private val prayerDataSource: KtorDataSource
) : PrayerRepository {
    override fun getPrayerByDate(
        year: Int,
        month: Int,
        day: Int,
        city: String
    ): Flow<List<Prayer>> =
        prayerDao.getPrayerByDate(year, month, day, city).map { prayers ->
            prayers.map { prayer -> prayer.toPrayer() }
        }

    override suspend fun insertPrayers(prayers: List<PrayerEntity>) =
        prayerDao.insertPrayers(prayers)

    override suspend fun sync(year: Int, month: Int, country: String, city: String): Result<Unit> {
        var result = Result.success(Unit)
        prayerDataSource.getPrayer(year, month, country, city)
            .onLeft { result = Result.failure(it) }
            .onRight { prayers ->
                val entities = prayers.map { prayer -> prayer.toEntity() }
                prayerDao.insertPrayers(entities)
            }

        return result
    }


}