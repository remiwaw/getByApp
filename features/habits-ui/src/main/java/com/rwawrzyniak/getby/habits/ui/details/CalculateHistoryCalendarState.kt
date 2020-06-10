package com.rwawrzyniak.getby.habits.ui.details

import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.ext.toDate
import com.rwawrzyniak.getby.models.HabitModel
import io.reactivex.Single
import java.util.Date

class CalculateHistoryCalendarState internal constructor(private val timeProvider: DateTimeProvider) {

	fun calculate(habit: HabitModel): Single<HistoryCalendarState> {
		return Single.just(
			HistoryCalendarState(
				maxDate = requireNotNull(timeProvider.getCurrentDate().plusDays(1).toDate()),
				selectedDates = filterByCheckStatus(habit),
				highlightedDates = listOf(),
				isSelectedDatesChanged = true
			)
		)
	}

	private fun filterByCheckStatus(
		habit: HabitModel
	): List<Date> {
		return habit.history.filter { it.checked }.map { it.day.toDate() }
	}

}
