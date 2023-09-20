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
import com.example.prayerwidget.domain.usecase.PRAYER_INTENT_ACTION
import com.example.prayerwidget.domain.usecase.getCurrentDay
import com.example.prayerwidget.domain.usecase.getCurrentDayInMillis
import com.example.prayerwidget.presentation.services.PRAYER_SERVICE_INTENT_ACTION
import com.example.prayerwidget.presentation.services.PrayerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != PRAYER_INTENT_ACTION)
            return
        goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            val settings = context?.dataStore?.data?.firstOrNull() ?: return@launch
            if (!settings.alarms.toList().any { it })
                return@launch
            val prayerDao = PrayerDatabase.createDatabase(context).prayerDao()
            val today = getCurrentDay()
            val prayer =
                prayerDao.getPrayerByDate(today.year, today.month, today.day, settings.city)
                    ?.toPrayer() ?: return@launch

            setPrayerAlarm(prayer, settings.alarms, context)
        }
    }

    private fun setPrayerAlarm(prayer: Prayer, alarms: List<Boolean>, context: Context) {
        val prayerTimes = getPrayerTimes(prayer, alarms)
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val serviceIntent = Intent(context, PrayerService::class.java).apply {
            action = PRAYER_SERVICE_INTENT_ACTION
        }
        prayerTimes.forEach { time ->
            try {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    PendingIntent.getService(
                        context,
                        1,
                        serviceIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    ),
                )
            } catch (exception: SecurityException) {
                Log.i("AlarmReceiver", "setPrayerAlarm: can't schedule exact alarms")
            }
        }
    }

    private fun getPrayerTimes(prayer: Prayer, alarms: List<Boolean>): List<Long> = buildList {
        val calendar = Calendar.getInstance()
        // todo
//        alarms::class.java.declaredFields.forEach { field ->
//            field.isAccessible = true
//            if (field.getBoolean(alarms))
//                add(getPrayerTimeInMillis(calendar, field.get(prayer) as String))
//        }
    }

    private fun getPrayerTimeInMillis(
        calendar: Calendar,
        time: String
    ): Long {
        val today = getCurrentDayInMillis()
        calendar.timeInMillis = today
        val (hours, minutes) = time
            .dropLast(6)
            .split(":")
            .map { it.toIntOrNull() ?: 0 }

        calendar.add(Calendar.HOUR_OF_DAY, hours)
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.timeInMillis
    }
}