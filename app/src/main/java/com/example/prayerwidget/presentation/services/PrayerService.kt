package com.example.prayerwidget.presentation.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

const val PRAYER_SERVICE_INTENT_ACTION = "PrayerService"

class PrayerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}