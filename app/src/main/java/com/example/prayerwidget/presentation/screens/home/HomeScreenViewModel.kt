package com.example.prayerwidget.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prayerwidget.domain.usecase.GetCurrentPrayerUseCase
import com.example.prayerwidget.domain.usecase.ObserveSettingsUseCase
import com.example.prayerwidget.domain.usecase.SyncUseCase
import com.example.prayerwidget.domain.usecase.UpdateSettingsUseCase
import com.example.prayerwidget.domain.usecase.calculateTimeLeft
import com.example.prayerwidget.presentation.model.toPrayerDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@KoinViewModel
class HomeScreenViewModel(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val syncUseCase: SyncUseCase,
    private val getCurrentPrayerUseCase: GetCurrentPrayerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        observeSettings()
        observePrayer()
        setPrayerTimeLeft()
    }

    fun handleEvent(event: HomeScreenEvent) = viewModelScope.launch {
        when (event) {
            is HomeScreenEvent.CityChanged -> _state.value = _state.value.copy(city = event.value)
            is HomeScreenEvent.CountryChanged -> _state.value =
                _state.value.copy(country = event.value)

            HomeScreenEvent.SyncClicked -> {
                updateSettingsUseCase {
                    it.copy(
                        city = _state.value.city,
                        country = _state.value.country
                    )
                }
                syncUseCase()
            }
        }
    }

    private fun setPrayerTimeLeft() = viewModelScope.launch {
        _state.map { it.prayer }.filterNotNull().collect { prayer ->
            val fajr = prayer.fajr.copy(timeLeft = calculateTimeLeft(prayer.fajr))
            val dhuhr = prayer.dhuhr.copy(timeLeft = calculateTimeLeft(prayer.dhuhr))
            val asr = prayer.asr.copy(timeLeft = calculateTimeLeft(prayer.asr))
            val maghrib = prayer.maghrib.copy(timeLeft = calculateTimeLeft(prayer.maghrib))
            val isha = prayer.isha.copy(timeLeft = calculateTimeLeft(prayer.isha))
            _state.value = _state.value.copy(
                prayer = prayer.copy(
                    fajr = fajr,
                    dhuhr = dhuhr,
                    asr = asr,
                    maghrib = maghrib,
                    isha = isha
                )
            )
            delay((1).toDuration(DurationUnit.MINUTES))
        }
    }


    private fun observePrayer() {
        viewModelScope.launch {
            getCurrentPrayerUseCase().collectLatest {
                _state.value = _state.value.copy(prayer = it.toPrayerDto())
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            observeSettingsUseCase().collectLatest {
                _state.value = _state.value.copy(
                    city = it.city,
                    country = it.country
                )
            }
        }
    }
}