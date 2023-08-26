package com.example.prayerwidget.data.work

import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.prayerwidget.data.source.PrayerRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.util.Calendar

class PrayerWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val prayerRepository: PrayerRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val (day, month, year) = getDate()
            val prayers = prayerRepository.getPrayerByDate(
                year = year,
                month = month,
                day = day,
                city = "Banha"
            ).firstOrNull()
            if (prayers?.isNotEmpty() == true)
                return Result.success()
            prayerRepository.sync(year, month, "EG", "Banha")
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun getDate(): Triple<Int, Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDate.now().plusDays(1)
            Triple(today.dayOfMonth, today.monthValue, today.year)
        } else {
            val today = Calendar.getInstance()
            today.add(Calendar.DAY_OF_MONTH, 1)
            Triple(
                today[Calendar.DAY_OF_MONTH],
                today[Calendar.MONTH],
                today[Calendar.YEAR]
            )
        }

    }
}