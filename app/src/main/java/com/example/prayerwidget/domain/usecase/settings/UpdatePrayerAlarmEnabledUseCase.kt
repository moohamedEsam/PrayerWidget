package com.example.prayerwidget.domain.usecase.settings

import android.content.Context
import com.example.prayerwidget.data.datastore.dataStore

fun interface UpdatePrayerAlarmEnabledUseCase : suspend (Int, Boolean) -> Unit


fun updatePrayerAlarmEnabledUseCase(context: Context) =
    UpdatePrayerAlarmEnabledUseCase { index, value ->
        context.dataStore.updateData {
            val alarms = it.alarms.mapIndexed { i, alarm ->
                if (i == index)
                    value
                else
                    alarm
            }
            it.copy(
                alarms = alarms
            )
        }
    }