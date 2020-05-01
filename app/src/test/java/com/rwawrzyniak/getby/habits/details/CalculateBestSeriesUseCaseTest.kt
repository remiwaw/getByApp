package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.habits.persistance.Frequency
import com.rwawrzyniak.getby.habits.persistance.Habit
import com.rwawrzyniak.getby.habits.persistance.HabitDay
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.time.LocalDate

class CalculateBestSeriesUseCaseTest {

	@Before
	fun setUp(){
		MockitoAnnotations.initMocks(this)
	}

    @Test
    fun shouldCalculateStrikeWithSortedByDaysInRow() {
		val expectedStrikes = listOf(
			Strike(LocalDate.of(2020, 12, 19), LocalDate.of(2020, 12, 21), 3),
			Strike(LocalDate.of(2020, 12, 16), LocalDate.of(2020, 12, 17), 2)
		)

		val history  = listOf(
			HabitDay(LocalDate.of(2020, 12, 16), checked = true),
			HabitDay(LocalDate.of(2020, 12, 17), checked = true),
			HabitDay(LocalDate.of(2020, 12, 18), checked = false),
			HabitDay(LocalDate.of(2020, 12, 19), checked = true),
			HabitDay(LocalDate.of(2020, 12, 20), checked = true),
			HabitDay(LocalDate.of(2020, 12, 21), checked = true),
			HabitDay(LocalDate.of(2020, 12, 22), checked = false),
			HabitDay(LocalDate.of(2020, 12, 23), checked = true)
		)

		val testHabit = testHabit(history)

		sut().calculateStrike(testHabit)
			.test()
			.assertValue{
				it[0] == expectedStrikes[0] &&
				it[1] == expectedStrikes[1]
			}
    }

	@Test
	fun shouldCalculateStrikeIfAndReturnEmptyListIfNoStrikes() {
		val history  = listOf(
			HabitDay(LocalDate.of(2020, 12, 16), checked = false),
			HabitDay(LocalDate.of(2020, 12, 17), checked = false),
			HabitDay(LocalDate.of(2020, 12, 18), checked = false)
		)

		val testHabit = testHabit(history)

		sut().calculateStrike(testHabit)
			.test()
			.assertValue{
				it.isEmpty()
			}
	}

	@Test
	fun shouldCalculateStrikeIfOnlyOneDate() {
		val expectedStrikes = listOf(
			Strike(LocalDate.of(2020, 12, 17), LocalDate.of(2020, 12, 17), 1)
		)

		val history  = listOf(
			HabitDay(LocalDate.of(2020, 12, 16), checked = false),
			HabitDay(LocalDate.of(2020, 12, 17), checked = true),
			HabitDay(LocalDate.of(2020, 12, 18), checked = false)
		)

		val testHabit = testHabit(history)

		sut().calculateStrike(testHabit)
			.test()
			.assertValue{
				it[0] == expectedStrikes[0]
			}
	}

	@Test
	fun shouldCalculateStrikeNoneIfOnlySingleDays() {
		val history  = listOf(
			HabitDay(LocalDate.of(2020, 12, 16), checked = false),
			HabitDay(LocalDate.of(2020, 12, 17), checked = true),
			HabitDay(LocalDate.of(2020, 12, 18), checked = false),
			HabitDay(LocalDate.of(2020, 12, 19), checked = true),
			HabitDay(LocalDate.of(2020, 12, 20), checked = false)
		)

		val testHabit = testHabit(history)

		sut().calculateStrike(testHabit)
			.test()
			.assertValue{
				it.isEmpty()
			}
	}

	@Test
	fun shouldCalculateStrikeWhenLastDayIsChecked() {
		val expectedStrikes = listOf(
			Strike(LocalDate.of(2020, 12, 20), LocalDate.of(2020, 12, 22), 3),
			Strike(LocalDate.of(2020, 12, 17), LocalDate.of(2020, 12, 18), 2)
		)

		val history  = listOf(
			HabitDay(LocalDate.of(2020, 12, 16), checked = false),
			HabitDay(LocalDate.of(2020, 12, 17), checked = true),
			HabitDay(LocalDate.of(2020, 12, 18), checked = true),
			HabitDay(LocalDate.of(2020, 12, 19), checked = false),
			HabitDay(LocalDate.of(2020, 12, 20), checked = true),
			HabitDay(LocalDate.of(2020, 12, 21), checked = true),
			HabitDay(LocalDate.of(2020, 12, 22), checked = true)
		)

		val testHabit = testHabit(history)

		sut().calculateStrike(testHabit)
			.test()
			.assertValue{
				it[0] == expectedStrikes[0] &&
				it[1] == expectedStrikes[1]
			}
	}

	@Test
	fun shouldCalculateThreeSetsOfStrikes() {
		val expectedStrikes = listOf(
			Strike(LocalDate.of(2020, 12, 14), LocalDate.of(2020, 12, 18), 5),
			Strike(LocalDate.of(2020, 12, 2), LocalDate.of(2020, 12, 4), 3),
			Strike(LocalDate.of(2020, 12, 10), LocalDate.of(2020, 12, 11), 2)
		)

		val history  = listOf(
			HabitDay(LocalDate.of(2020, 12, 2), checked = true),
			HabitDay(LocalDate.of(2020, 12, 3), checked = true),
			HabitDay(LocalDate.of(2020, 12, 4), checked = true),
			HabitDay(LocalDate.of(2020, 12, 10), checked = true),
			HabitDay(LocalDate.of(2020, 12, 11), checked = true),
			HabitDay(LocalDate.of(2020, 12, 14), checked = true),
			HabitDay(LocalDate.of(2020, 12, 15), checked = true),
			HabitDay(LocalDate.of(2020, 12, 16), checked = true),
			HabitDay(LocalDate.of(2020, 12, 17), checked = true),
			HabitDay(LocalDate.of(2020, 12, 18), checked = true)
		)

		val testHabit = testHabit(history)

		sut().calculateStrike(testHabit)
			.test()
			.assertValue{
				it[0] == expectedStrikes[0] &&
				it[1] == expectedStrikes[1] &&
				it[2] == expectedStrikes[2]
			}
	}

	private fun testHabit(history: List<HabitDay>): Habit = Habit(
		name = "name",
		description = "description",
		frequency = Frequency(times = 3, cycle = 7),
		reminder = null,
		history = history
	)

	private fun sut() = CalculateBestSeriesUseCase()
}
