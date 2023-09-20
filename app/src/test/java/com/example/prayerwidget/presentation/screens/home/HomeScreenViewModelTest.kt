package com.example.prayerwidget.presentation.screens.home

import app.cash.turbine.test
import com.example.prayerwidget.data.datastore.Settings
import com.example.prayerwidget.domain.model.Prayer
import com.example.prayerwidget.domain.model.empty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {
    @OptIn(DelicateCoroutinesApi::class)
    private val thread = newSingleThreadContext("ui Thread")
    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var settingsFlow: MutableStateFlow<Settings>
    private lateinit var prayerFlow: MutableStateFlow<Prayer>


    @Before
    fun setUp() {
        Dispatchers.setMain(thread)
        settingsFlow = MutableStateFlow(Settings())
        prayerFlow = MutableStateFlow(Prayer.empty())
        viewModel = HomeScreenViewModel(
            observeSettingsUseCase = { settingsFlow },
            updateSettingsUseCase = { settingsFlow.value = it(settingsFlow.value) },
            syncUseCase = {},
            getCurrentPrayerUseCase = { prayerFlow }
        )
    }

    @Test
    fun `toggling the prayer should update the prayer state enabled`() = runTest {
        val state = viewModel.state.filter { it.prayer != null }.first()
        viewModel.handleEvent(
            HomeScreenEvent.PrayerAlarmEnableToggle(
                state.prayer!!.prayers.first().copy(alarmEnabled = true)
            )

        ).join()
        viewModel.state.test {
            val item = awaitItem()
            item.prayer?.prayers?.first()?.alarmEnabled shouldBe true
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        thread.close()
    }
}