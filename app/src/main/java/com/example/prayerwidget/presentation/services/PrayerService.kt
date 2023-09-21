package com.example.prayerwidget.presentation.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.prayerwidget.R
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.domain.usecase.GetCurrentPrayerUseCase
import com.example.prayerwidget.presentation.broadcasts.getNextPrayer
import com.example.prayerwidget.presentation.broadcasts.scheduleExactAlarm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

const val PRAYER_SERVICE_INTENT_ACTION = "PrayerServiceStart"
const val PRAYER_SERVICE_STOP_ACTION = "PrayerServiceStop"

private const val CHANNEL_ID = "1"

class PrayerService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private val getCurrentPrayerUseCase by inject<GetCurrentPrayerUseCase>()
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!::mediaPlayer.isInitialized)
            mediaPlayer = MediaPlayer.create(this, R.raw.call_to_prayer)

        when (intent?.action) {
            PRAYER_SERVICE_INTENT_ACTION -> {
                mediaPlayer.start()
            }

            PRAYER_SERVICE_STOP_ACTION -> {
                mediaPlayer.reset()
                mediaPlayer.release()
                stopForeground(STOP_FOREGROUND_REMOVE)
            }
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, getForegroundNotification())
        CoroutineScope(Dispatchers.IO).launch {
            val prayer = getCurrentPrayerUseCase().firstOrNull() ?: return@launch
            val alarms = dataStore.data.map { it.alarms }.firstOrNull() ?: return@launch
            val nextPrayer = getNextPrayer(prayer, alarms)
            scheduleExactAlarm(nextPrayer)
        }
    }

    private fun getForegroundNotification(): Notification {
        val stopIntent = Intent(this, PrayerService::class.java).run {
            action = PRAYER_SERVICE_STOP_ACTION
            PendingIntent.getService(
                this@PrayerService,
                1,
                this,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Prayer widget")
            .setContentText("call to prayer")
            .setAutoCancel(true)
            .addAction(R.drawable.ic_launcher_foreground, "Stop", stopIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

}

fun NotificationManager.createCallToPrayerChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val channel = NotificationChannel(
        CHANNEL_ID,
        "call to prayer",
        NotificationManager.IMPORTANCE_LOW
    )

    createNotificationChannel(channel)
}
