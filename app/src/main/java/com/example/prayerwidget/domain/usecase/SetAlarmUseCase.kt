package com.example.prayerwidget.domain.usecase

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.prayerwidget.domain.timeInMillis
import com.example.prayerwidget.presentation.broadcasts.AlarmReceiver
import com.example.prayerwidget.presentation.di.checkIfAlarmIsActive
import java.time.LocalDate

const val PRAYER_INTENT_ACTION = "PrayerIntent"

fun interface SetAlarmUseCase : () -> Unit


fun setAlarmUseCase(context: Context) = SetAlarmUseCase {
    val intent = Intent(context, AlarmReceiver::class.java).apply { action = PRAYER_INTENT_ACTION }
    val isActive = checkIfAlarmIsActive(context, intent)
    if (isActive) return@SetAlarmUseCase
    val alarmManager = context.getSystemService(AlarmManager::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        /* context = */ context,
        /* requestCode = */ 0,
        /* intent = */ intent,
        /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val localDateTime = LocalDate.now()
        .atStartOfDay()
        .withHour(1)

    alarmManager.setInexactRepeating(
        /* type = */ AlarmManager.RTC,
        /* triggerAtMillis = */ localDateTime.timeInMillis(),
        /* intervalMillis = */ AlarmManager.INTERVAL_FIFTEEN_MINUTES,
        /* operation = */ pendingIntent
    )
}