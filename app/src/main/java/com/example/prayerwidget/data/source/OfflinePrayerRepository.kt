package com.example.prayerwidget.data.source

import com.example.prayerwidget.data.model.toEntity
import com.example.prayerwidget.data.model.toPrayer
import com.example.prayerwidget.data.source.local.PrayerDao
import com.example.prayerwidget.data.source.remote.KtorDataSource
import com.example.prayerwidget.domain.model.Prayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory


@Factory
class OfflinePrayerRepository(
    private val prayerDao: PrayerDao,
    private val prayerDataSource: KtorDataSource
) : PrayerRepository {

    override fun getPrayerByDateFlow(year: Int, month: Int, day: Int, city: String): Flow<Prayer> =
        prayerDao.getPrayerByDateFlow(year, month, day, city)
            .filterNotNull()
            .map { prayerEntity -> prayerEntity.toPrayer() }

    override suspend fun insertPrayers(prayers: List<Prayer>) =
        prayerDao.insertPrayers(prayers.map { it.toEntity() })

    override suspend fun getPrayerByDate(year: Int, month: Int, day: Int, city: String): Prayer? =
        prayerDao.getPrayerByDate(year, month, day, city)?.toPrayer()

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