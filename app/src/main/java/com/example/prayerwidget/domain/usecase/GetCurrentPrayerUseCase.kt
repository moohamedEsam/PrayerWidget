package com.example.prayerwidget.domain.usecase

import android.content.Context
import com.example.prayerwidget.data.datastore.dataStore
import com.example.prayerwidget.data.source.PrayerRepository
import com.example.prayerwidget.domain.model.Prayer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate

fun interface GetCurrentPrayerUseCase : () -> Flow<Prayer>

@OptIn(ExperimentalCoroutinesApi::class)
fun getCurrentPrayerUseCase(context: Context, prayerRepository: PrayerRepository) =
    GetCurrentPrayerUseCase {
        val localDate = LocalDate.now()
        context.dataStore
            .data.map { it.city }
            .flatMapLatest { city ->
                prayerRepository.getPrayerByDateFlow(
                    localDate.year,
                    localDate.monthValue,
                    localDate.dayOfMonth,
                    city
                )
            }
    }