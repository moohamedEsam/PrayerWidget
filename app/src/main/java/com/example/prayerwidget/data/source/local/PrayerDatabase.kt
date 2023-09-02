package com.example.prayerwidget.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prayerwidget.data.model.PrayerEntity

@Database(entities = [PrayerEntity::class], version = 1, exportSchema = false)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao

    companion object {
        const val DATABASE_NAME = "prayer_database"

        fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            PrayerDatabase::class.java,
            DATABASE_NAME
        ).allowMainThreadQueries().build()
    }
}