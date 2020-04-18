package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.core.ext.toInt
import com.rwawrzyniak.getby.habits.HabitsRepository
import com.rwawrzyniak.getby.habits.getHabitDaysInCycle
import io.reactivex.Single
import java.time.LocalDate
import javax.inject.Inject

class CalculateHabitDayScoreUseCase @Inject constructor(private val habitsRepository: HabitsRepository) {
	fun calculateScoreForDay(habitId: String, givenDay: LocalDate): Single<Int> =
		habitsRepository.getSingle(habitId)
			.map { habit -> val totalPointsInCycle = habit.getHabitDaysInCycle(givenDay).sumBy { habitDay -> habitDay.checked.toInt() }
				if(totalPointsInCycle > habit.frequency.times) habit.frequency.times else totalPointsInCycle
			}
}
