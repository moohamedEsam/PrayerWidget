package com.example.prayerwidget.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prayerwidget.domain.usecase.transformTime
import com.example.prayerwidget.presentation.model.PrayerDto
import com.example.prayerwidget.presentation.model.SinglePrayer
import com.example.prayerwidget.presentation.model.empty
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun HomeScreen(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = state.country,
                onValueChange = { onEvent(HomeScreenEvent.CountryChanged(it)) },
                modifier = Modifier.weight(1f),
                label = { Text(text = "Country Code") }
            )

            TextField(
                value = state.city,
                onValueChange = { onEvent(HomeScreenEvent.CityChanged(it)) },
                modifier = Modifier.weight(1f),
                label = { Text(text = "City") }
            )
        }
        Button(
            onClick = { onEvent(HomeScreenEvent.SyncClicked) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Sync")
        }
        if (state.prayer == null) return@Column
        Text(
            text = "Prayer Times ${state.prayer.month}/${state.prayer.day}",
            style = MaterialTheme.typography.headlineMedium
        )
        SinglePrayerItem(state.prayer.fajr)
        SinglePrayerItem(state.prayer.dhuhr)
        SinglePrayerItem(state.prayer.asr)
        SinglePrayerItem(state.prayer.maghrib)
        SinglePrayerItem(state.prayer.isha)

    }
}

@Composable
private fun SinglePrayerItem(singlePrayer: SinglePrayer) {
    ListItem(
        headlineContent = { Text(text = singlePrayer.name) },
        trailingContent = {
            Text(
                text = transformTime(singlePrayer.time),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = { Text(text = "${singlePrayer.timeLeft} Left") },
        leadingContent = { Checkbox(checked = false, onCheckedChange = {}) }
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    Surface {
        HomeScreen(state = HomeScreenState(
            prayer = PrayerDto.empty()
        ), onEvent = {})
    }

}