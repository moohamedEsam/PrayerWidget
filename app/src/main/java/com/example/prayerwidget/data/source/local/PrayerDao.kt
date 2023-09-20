package com.example.prayerwidget.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prayerwidget.data.model.PrayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {

    @Query("SELECT * FROM prayerEntity WHERE year = :year and month =:month and day =:day AND city = :city")
    fun getPrayerByDateFlow(year: Int, month: Int, day: Int, city: String): Flow<PrayerEntity?>

    @Query("select * from prayerEntity where year = :year and month = :month and day = :day and city = :city")
    suspend fun getPrayerByDate(year: Int, month: Int, day: Int, city: String): PrayerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayers(prayers: List<PrayerEntity>)

    @Query("delete from prayerEntity")
    suspend fun deleteAllPrayers()
}