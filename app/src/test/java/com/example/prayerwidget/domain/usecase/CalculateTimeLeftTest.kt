package com.example.prayerwidget.domain.usecase

import com.example.prayerwidget.domain.calculateTimeLeft
import com.example.prayerwidget.presentation.model.SinglePrayer
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class CalculateTimeLeftTest {

    @Test
    fun `prayer after 4h from now should return 4h duration`() = runTest {
        //arrange
        val dateTime = LocalDateTime.now().plusHours(4)
        val prayer = SinglePrayer(time = "${dateTime.hour}:${dateTime.minute} (EEST)")

        //act
        val result = calculateTimeLeft(prayer)

        //assert
        result shouldBeGreaterThanOrEqualTo (3.95).toDuration(DurationUnit.HOURS)
        result shouldBeLessThanOrEqualTo (4).toDuration(DurationUnit.HOURS)
    }

    @Test
    fun `prayer after 3h 15m from now should return 4h duration`() = runTest {
        //arrange
        val dateTime = LocalDateTime.now().plusHours(3).plusMinutes(15)
        val prayer = SinglePrayer(time = "${dateTime.hour}:${dateTime.minute} (EEST)")

        //act
        val result = calculateTimeLeft(prayer)

        //assert
        result shouldBeGreaterThanOrEqualTo (3).toDuration(DurationUnit.HOURS) + (10).toDuration(
            DurationUnit.MINUTES
        )
        result shouldBeLessThanOrEqualTo (3).toDuration(DurationUnit.HOURS) + (15).toDuration(
            DurationUnit.MINUTES
        )
    }

}