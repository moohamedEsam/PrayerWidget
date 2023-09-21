package com.example.prayerwidget.data.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.WorkerParameters
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.source.PrayerRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

class PrayerWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val prayerRepository: PrayerRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val localDate = LocalDate.now().plusDays(1)
            val settings = context.dataStore.data.firstOrNull() ?: return Result.failure()
            val prayer = prayerRepository.getPrayerByDate(
                year = localDate.year,
                month = localDate.monthValue,
                day = localDate.dayOfMonth,
                city = settings.city
            )
            if (prayer != null) return Result.success()
            prayerRepository.sync(
                localDate.year,
                localDate.monthValue,
                settings.country,
                settings.city
            )
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        fun constraints() = Constraints(requiredNetworkType = NetworkType.CONNECTED)
        const val TAG = "prayerWorkerTag"
    }
}