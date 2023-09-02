package com.example.prayerwidget.data.datastore

import android.content.Context
import androidx.datastore.dataStore

val Context.dataStore by dataStore(
    fileName = "settings.json",
    serializer = SettingsSerializer
)