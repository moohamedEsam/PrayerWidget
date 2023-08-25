package com.example.prayerwidget.data.work

import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.prayerwidget.data.source.PrayerRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

class PrayerWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val prayerRepository: PrayerRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val today = LocalDate.now().plusDays(1)
                val prayers = prayerRepository.getPrayerByDate(
                    year = today.year,
                    month = today.monthValue,
                    day = today.dayOfMonth,
                    city = "Banha"
                ).firstOrNull()
                if (prayers?.isNotEmpty() == true)
                    return Result.success()

            }
            prayerRepository.sync(2023, 8, "EG", "Banha")
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}