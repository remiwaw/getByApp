package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.core.ext.date.datesBetweenExcludingStartIncludingEnd
import com.rwawrzyniak.getby.core.ext.toInt
import com.rwawrzyniak.getby.habits.DayScore
import com.rwawrzyniak.getby.habits.HabitsRepository
import com.rwawrzyniak.getby.habits.getHabitDaysInCycle
import io.reactivex.Single
import java.time.LocalDate
import javax.inject.Inject

class CalculateHabitDayScoreUseCase @Inject constructor(private val habitsRepository: HabitsRepository) {
	fun calculateScoreForDayRange(
		habitId: String,
		startDate: LocalDate,
		endDate: LocalDate
	): Single<List<DayScore>> =
		habitsRepository.getSingle(habitId)
			.map { habit ->
				datesBetweenExcludingStartIncludingEnd(startDate, endDate).map { givenDay ->
					val totalPointsInCycle = habit.getHabitDaysInCycle(givenDay)
						.sumBy { habitDay -> habitDay.checked.toInt() }
					val validTotalPoints =
						if (totalPointsInCycle > habit.frequency.times)
							habit.frequency.times else totalPointsInCycle

					DayScore(givenDay, validTotalPoints)
				}
			}
}
