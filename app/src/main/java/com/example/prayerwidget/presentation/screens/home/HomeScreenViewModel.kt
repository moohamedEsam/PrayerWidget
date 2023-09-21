package com.example.prayerwidget.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prayerwidget.domain.calculateTimeLeft
import com.example.prayerwidget.domain.usecase.GetCurrentPrayerUseCase
import com.example.prayerwidget.domain.usecase.ObserveSettingsUseCase
import com.example.prayerwidget.domain.usecase.SyncUseCase
import com.example.prayerwidget.domain.usecase.settings.UpdatePrayerAlarmEnabledUseCase
import com.example.prayerwidget.domain.usecase.settings.UpdateSettingsUseCase
import com.example.prayerwidget.presentation.model.toPrayerDto
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@KoinViewModel
class HomeScreenViewModel(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val updatePrayerAlarmEnabledUseCase: UpdatePrayerAlarmEnabledUseCase,
    private val syncUseCase: SyncUseCase,
    private val getCurrentPrayerUseCase: GetCurrentPrayerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()
    private val prayerDtoFlow = combine(
        getCurrentPrayerUseCase(),
        observeSettingsUseCase()
    ) { prayer, settings ->
        _state.update { it.copy(city = settings.city, country = settings.country) }
        val prayerDto = prayer.toPrayerDto()
        prayerDto.prayers.forEachIndexed { index, singlePrayer ->
            singlePrayer.alarmEnabled = settings.alarms[index]
        }
        prayerDto
    }

    init {
        observePrayer()
        setPrayerTimeLeft()
    }

    fun handleEvent(event: HomeScreenEvent) = viewModelScope.launch {
        when (event) {
            is HomeScreenEvent.CityChanged -> _state.value = _state.value.copy(city = event.value)
            is HomeScreenEvent.CountryChanged -> _state.value =
                _state.value.copy(country = event.value)

            is HomeScreenEvent.PrayerAlarmEnableToggle -> updatePrayerAlarmEnabledUseCase(
                event.index,
                event.enabled
            )

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

    @OptIn(FlowPreview::class)
    private fun setPrayerTimeLeft() = viewModelScope.launch {
        _state.map { it.prayer?.prayers }
            .debounce((1).toDuration(DurationUnit.MINUTES))
            .filterNotNull()
            .collectLatest { prayers ->
                Log.i("HomeScreenViewModel", "setPrayerTimeLeft: called")
                prayers.forEach {
                    it.timeLeft = calculateTimeLeft(it)
                }
                _state.update {
                    it.copy(
                        prayer = it.prayer?.copy(prayers = prayers.toMutableStateList())
                    )
                }
            }
    }


    private fun observePrayer() {
        viewModelScope.launch {
            prayerDtoFlow.collectLatest { prayerDto ->
                Log.i("HomeScreenViewModel", "observePrayer: called")
                _state.update { state ->
                    prayerDto.prayers.forEachIndexed { index, singlePrayer ->
                        singlePrayer.timeLeft =
                            state.prayer?.prayers?.getOrNull(index)?.timeLeft ?: Duration.ZERO
                    }
                    state.copy(prayer = prayerDto)
                }
            }
        }
    }

}