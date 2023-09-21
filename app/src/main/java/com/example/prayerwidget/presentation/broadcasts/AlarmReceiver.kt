package com.example.prayerwidget.presentation.broadcasts

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.model.toPrayer
import com.example.prayerwidget.data.source.local.PrayerDatabase
import com.example.prayerwidget.domain.model.Prayer
import com.example.prayerwidget.domain.model.toList
import com.example.prayerwidget.domain.timeInMillis
import com.example.prayerwidget.domain.usecase.PRAYER_BROADCAST_INTENT_ACTION
import com.example.prayerwidget.presentation.services.PRAYER_SERVICE_INTENT_ACTION
import com.example.prayerwidget.presentation.services.PrayerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != PRAYER_BROADCAST_INTENT_ACTION)
            return
        goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            val settings = context?.dataStore?.data?.firstOrNull() ?: return@launch
            if (!settings.alarms.any { it })
                return@launch
            val prayerDao = PrayerDatabase.createDatabase(context).prayerDao()
            val localDate = LocalDate.now()
            val prayer = prayerDao.getPrayerByDate(
                localDate.year,
                localDate.monthValue,
                localDate.dayOfMonth,
                settings.city
            )?.toPrayer() ?: return@launch

            setPrayerAlarm(prayer, settings.alarms, context)
        }
    }

    private fun setPrayerAlarm(prayer: Prayer, alarms: List<Boolean>, context: Context) {
        val prayerTime = getNextPrayer(prayer, alarms)
        context.scheduleExactAlarm(prayerTime)
    }

}


fun Context.scheduleExactAlarm(
    prayerTime: Long
) {
    val alarmManager = getSystemService(AlarmManager::class.java)
    val serviceIntent = Intent(this, PrayerService::class.java).apply {
        action = PRAYER_SERVICE_INTENT_ACTION
    }
    try {
        alarmManager.setExact(
            AlarmManager.RTC,
            prayerTime,
            PendingIntent.getService(
                this,
                1,
                serviceIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ),
        )
    } catch (exception: SecurityException) {
        Log.i("AlarmReceiver", "setPrayerAlarm: can't schedule exact alarms")
    }
}


fun getNextPrayer(prayer: Prayer, alarms: List<Boolean>): Long {
    val prayerTimeString = prayer.toList()
        .filterIndexed { index, _ -> alarms[index] }
        .first { getPrayerTimeInMillis(it) > LocalDateTime.now().timeInMillis() }

    return getPrayerTimeInMillis(prayerTimeString)
}

fun getPrayerTimeInMillis(time: String): Long {
    val (hours, minutes) = time
        .dropLast(7)
        .split(":")
        .map { it.toIntOrNull() ?: 0 }

    val localDateTime = LocalDate.now()
        .atStartOfDay()
        .withHour(hours)
        .withMinute(minutes)

    return localDateTime.timeInMillis()
}
