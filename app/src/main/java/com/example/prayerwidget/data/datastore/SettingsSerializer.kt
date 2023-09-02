package com.example.prayerwidget.data.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings
        get() = Settings()

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): Settings {
        return try {
            Json.decodeFromStream(input)
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: Settings, output: OutputStream) {
        Json.encodeToStream(t, output)
    }
}