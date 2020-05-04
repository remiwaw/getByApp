package com.rwawrzyniak.getby.habits

import com.rwawrzyniak.getby.habits.details.CalculateHabitDayScoreUseCase
import com.rwawrzyniak.getby.habits.persistance.DayScore
import com.rwawrzyniak.getby.habits.persistance.Frequency
import com.rwawrzyniak.getby.habits.persistance.Habit
import com.rwawrzyniak.getby.habits.persistance.HabitDay
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.time.LocalDate

internal class CalculateDayScoreUseCaseTest {

	@Before
	fun setUp(){
		MockitoAnnotations.initMocks(this)
	}

	@Test
    fun calculateScoreForDayNotExceedingMaximumTimesPerCycle() {
		val startDate = LocalDate.of(2020,12,19)
		val endDate = LocalDate.of(2020,12,21)
		val expectedDayScoreList = listOf(
			DayScore(
				LocalDate.of(
					2020,
					12,
					20
				), 66
			),
			DayScore(
				LocalDate.of(
					2020,
					12,
					21
				), 100
			)
		)
		val frequency = Frequency(
			times = 3,
			cycle = 7
		)
		val testHabit = Habit(
			name = "name",
			description = "description",
			frequency = frequency,
			reminder = null,
			history  = listOf(
				HabitDay(LocalDate.of(2020, 12, 23), checked = false),
				HabitDay(LocalDate.of(2020, 12, 22), checked = false),
				HabitDay(LocalDate.of(2020, 12, 21), checked = true),
				HabitDay(LocalDate.of(2020, 12, 20), checked = false),
				HabitDay(LocalDate.of(2020, 12, 19), checked = true),
				HabitDay(LocalDate.of(2020, 12, 18), checked = false),
				HabitDay(LocalDate.of(2020, 12, 17), checked = false),
				HabitDay(LocalDate.of(2020, 12, 16), checked = true)
		)
		)

		sut().calculateScoreForDayRangeExcludingStart(testHabit, startDate, endDate)
			.test()
			.assertValue(expectedDayScoreList)
	}


	@Test
	fun calculateScoreForDayExceedingMaximumTimesPerCycle() {
		val startDate = LocalDate.of(2020,12,19)
		val endDate = LocalDate.of(2020,12,21)
		val expectedDayScoreList = listOf(
			DayScore(
				LocalDate.of(
					2020,
					12,
					20
				), 100
			),
			DayScore(
				LocalDate.of(
					2020,
					12,
					21
				), 100
			)
		)
		val frequency = Frequency(
			times = 3,
			cycle = 7
		)
		val testHabit = Habit(
			name = "name",
			description = "description",
			frequency = frequency,
			reminder = null,
			history = listOf(
				HabitDay(LocalDate.of(2020, 12, 23), checked = true),
				HabitDay(LocalDate.of(2020, 12, 22), checked = true),
				HabitDay(LocalDate.of(2020, 12, 21), checked = true),
				HabitDay(LocalDate.of(2020, 12, 20), checked = true),
				HabitDay(LocalDate.of(2020, 12, 19), checked = true),
				HabitDay(LocalDate.of(2020, 12, 18), checked = true),
				HabitDay(LocalDate.of(2020, 12, 17), checked = true),
				HabitDay(LocalDate.of(2020, 12, 16), checked = true)
			)
		)

		sut().calculateScoreForDayRangeExcludingStart(testHabit, startDate, endDate)
			.test()
			.assertValue(expectedDayScoreList)
	}

	private fun sut() =
		CalculateHabitDayScoreUseCase()
}