package com.rwawrzyniak.getby.habits

import com.rwawrzyniak.getby.core.ext.datesBetweenExcludingStartIncludingEnd
import com.rwawrzyniak.getby.core.ext.toInt
import com.rwawrzyniak.getby.models.DayScore
import com.rwawrzyniak.getby.models.HabitModel
import com.rwawrzyniak.getby.models.getHabitDaysInCycle
import io.reactivex.Single
import java.time.LocalDate
import javax.inject.Inject

class CalculateHabitDayScoreUseCase @Inject internal constructor() {
	fun calculateScoreForDayRangeExcludingStart(
		habit: HabitModel,
		startDate: LocalDate,
		endDate: LocalDate
	): Single<List<DayScore>> =
		Single.just(habit)
			.map { habit ->
				datesBetweenExcludingStartIncludingEnd(
					startDate,
					endDate
				).map { givenDay ->
					val totalPointsInCycle = habit.getHabitDaysInCycle(givenDay)
						.sumBy { habitDay -> habitDay.checked.toInt() }
					val validTotalPoints =
						if (totalPointsInCycle > habit.frequency.times)
							habit.frequency.times else totalPointsInCycle

					val fulfilledPercentage = ((validTotalPoints.toDouble()/habit.frequency.times.toDouble())*100).toInt()
					DayScore(
						givenDay,
						fulfilledPercentage
					)
				}
			}
}

