package com.example.prayerwidget.domain.usecase

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.prayerwidget.domain.timeInMillis
import com.example.prayerwidget.presentation.broadcasts.AlarmReceiver
import java.time.LocalDate

const val PRAYER_BROADCAST_INTENT_ACTION = "PrayerBroadCastIntent"

fun interface SetAlarmUseCase : () -> Unit


fun setAlarmUseCase(context: Context) = SetAlarmUseCase {
    val intent = Intent(context, AlarmReceiver::class.java)
        .apply { action = PRAYER_BROADCAST_INTENT_ACTION }
    val alarmManager = context.getSystemService(AlarmManager::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        /* context = */ context,
        /* requestCode = */ 1,
        /* intent = */ intent,
        /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val isActive = checkIfAlarmIsActive(context, intent)
    if (isActive) return@SetAlarmUseCase
    val localDateTime = LocalDate.now()
        .atStartOfDay()
        .withHour(1)

    alarmManager.setInexactRepeating(
        /* type = */ AlarmManager.RTC,
        /* triggerAtMillis = */ localDateTime.timeInMillis(),
        /* intervalMillis = */ AlarmManager.INTERVAL_DAY,
        /* operation = */ pendingIntent
    )
}

fun checkIfAlarmIsActive(context: Context, intent: Intent) = PendingIntent.getBroadcast(
    context,
    1,
    intent,
    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
) != null