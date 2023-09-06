package com.example.prayerwidget.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.source.PrayerRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class PrayerWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val prayerRepository: PrayerRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            context.dataStore.updateData {
                it.copy(city = "Banha", country = "EG")
            }
            val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val settings = context.dataStore.data.firstOrNull()
            val country = settings?.country ?: "EG"
            val city = settings?.city ?: "Banha"
            val prayer = prayerRepository.getPrayerByDate(
                year = year,
                month = month,
                day = calendar[Calendar.DAY_OF_MONTH],
                city = city
            )
            if (prayer != null) return Result.success()
            prayerRepository.sync(year, month, country, city)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}