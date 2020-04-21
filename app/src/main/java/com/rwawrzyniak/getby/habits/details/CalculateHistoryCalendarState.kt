package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.ext.date.toDate
import com.rwawrzyniak.getby.habits.Habit
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class CalculateHistoryCalendarState @Inject internal constructor(private val timeProvider: DateTimeProvider) {

	fun calculate(habit: Habit): Single<HistoryCalendarState> {
		return Single.just(HistoryCalendarState(
			maxDate = requireNotNull(timeProvider.getCurrentDate().toDate()),
			selectedDates = filterByCheckStatus(habit),
			highlightedDates = listOf(),
			isChanged = true
		))
	}

	private fun filterByCheckStatus(
		habit: Habit
	): List<Date> {
		return habit.history.filter { it.checked }.map { it.day.toDate() }
	}

}
