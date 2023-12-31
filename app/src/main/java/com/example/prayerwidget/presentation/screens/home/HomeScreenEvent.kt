package com.example.prayerwidget.presentation.screens.home

sealed interface HomeScreenEvent {
    data class CityChanged(val value: String) : HomeScreenEvent
    data class CountryChanged(val value: String) : HomeScreenEvent
    data class PrayerAlarmEnableToggle(val index: Int, val enabled: Boolean) : HomeScreenEvent
    data object SyncClicked : HomeScreenEvent
}