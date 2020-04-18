package com.rwawrzyniak.getby.habits.details

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.rwawrzyniak.getby.habits.Frequency
import com.rwawrzyniak.getby.habits.Habit
import com.rwawrzyniak.getby.habits.HabitDay
import com.rwawrzyniak.getby.habits.HabitsRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.time.LocalDate

internal class CalculateHabitDayScoreUseCaseTest {

	@Mock
	private lateinit var repository: HabitsRepository


	@Before
	fun setUp(){
		MockitoAnnotations.initMocks(this)
	}

	@Test
    fun calculateScoreForDayNotExceedingMaximumTimesPerCycle() {
		val expectedScore = 3
		val testHabitId = "12345"
		val testLocalDate = LocalDate.of(2020,12,20)
		val frequency = Frequency(
			times = 3,
			cycle = 7
		)
		val testHabit = Habit(name ="name", description = "description", frequency = frequency, reminder = null, history = listOf(
			HabitDay(LocalDate.of(2020,12,20), 0, checked = false),
			HabitDay(LocalDate.of(2020,12,19), 0, checked = true),
			HabitDay(LocalDate.of(2020,12,18), 0, checked = true),
			HabitDay(LocalDate.of(2020,12,17), 0, checked = false),
			HabitDay(LocalDate.of(2020,12,16), 0, checked = true)
		))
		repository.stub {
			on { getSingle(any()) } doReturn Single.just(testHabit)
		}

		val score = sut().calculateScoreForDay(testHabitId, testLocalDate)
			.test()
			.assertValue(expectedScore)
	}


	@Test
	fun calculateScoreForDayExceedingMaximumTimesPerCycle() {
		val testHabitId = "12345"
		val testLocalDate = LocalDate.of(2020,12,20)
		val frequency = Frequency(
			times = 3,
			cycle = 7
		)
		val testHabit = Habit(name ="name", description = "description", frequency = frequency, reminder = null, history = listOf(
			HabitDay(LocalDate.of(2020,12,20), 0, checked = true),
			HabitDay(LocalDate.of(2020,12,19), 0, checked = true),
			HabitDay(LocalDate.of(2020,12,18), 0, checked = true),
			HabitDay(LocalDate.of(2020,12,17), 0, checked = true),
			HabitDay(LocalDate.of(2020,12,16), 0, checked = true)
		))
		repository.stub {
			on { getSingle(any()) } doReturn Single.just(testHabit)
		}

		val score = sut().calculateScoreForDay(testHabitId, testLocalDate)
			.test()
			.assertValue(frequency.times)
	}

	private fun sut() = CalculateHabitDayScoreUseCase(repository)
}
