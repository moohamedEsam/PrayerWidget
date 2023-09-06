package com.example.prayerwidget.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.prayerwidget.MainActivity
import com.example.prayerwidget.R
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.model.toPrayer
import com.example.prayerwidget.data.source.local.PrayerDatabase
import com.example.prayerwidget.domain.model.Prayer
import com.example.prayerwidget.domain.usecase.getCurrentDay
import com.example.prayerwidget.domain.usecase.transformTime
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class PrayerWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val city = context.dataStore.data.map { it.city }.firstOrNull() ?: "Banha"
        val prayerDao = PrayerDatabase.createDatabase(context).prayerDao()
        val today = getCurrentDay()
        val prayer = prayerDao.getPrayerByDate(today.year, today.month, today.day, city)
            ?.toPrayer() ?: return
        provideContent {
            GlanceTheme {
                PrayerContent(prayer)
            }
        }
    }

    @Composable
    private fun PrayerContent(prayer: Prayer) {
        val style = TextStyle(fontSize = 16.sp, color = GlanceTheme.colors.onSecondaryContainer)
        Column(
            modifier = GlanceModifier.background(GlanceTheme.colors.secondaryContainer)
                .cornerRadius(5.dp)
                .clickable(onClick = actionStartActivity<MainActivity>())
                .padding(32.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Prayer Times ${prayer.month}/${prayer.day}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = GlanceTheme.colors.onSecondaryContainer
                    ),
                    modifier = GlanceModifier.padding(bottom = 16.dp).defaultWeight()
                )
                Image(
                    provider = ImageProvider(resId = R.drawable.ic_refresh_foreground),
                    contentDescription = "Refresh",
                    modifier = GlanceModifier.size(40.dp)
                        .clickable(onClick = actionRunCallback<RefreshAction>()),
                    colorFilter = ColorFilter.tint(GlanceTheme.colors.onSecondaryContainer)
                )
            }
            LazyColumn(
                modifier = GlanceModifier.defaultWeight()
            ) {
                item { PrayerItem("الفجر", prayer.fajr, style) }
                item { PrayerItem("الظهر", prayer.dhuhr, style) }
                item { PrayerItem("العصر", prayer.asr, style) }
                item { PrayerItem("المغرب", prayer.maghrib, style) }
                item { PrayerItem("العشاء", prayer.isha, style) }
            }
        }
    }

    @Composable
    private fun PrayerItem(prayerTitle: String, prayerTime: String, style: TextStyle) {
        Row(
            modifier = GlanceModifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = transformTime(prayerTime),
                style = style,
                modifier = GlanceModifier.defaultWeight()
            )
            Text(
                text = prayerTitle,
                style = style,
            )
        }
    }
}

class PrayerWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = PrayerWidget()
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        PrayerWidget().update(context, glanceId)
    }
}