package com.rwawrzyniak.getby.habits

import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.ext.date.getLastNDays
import java.time.LocalDate
import javax.inject.Inject

class HabitHolderHelper @Inject constructor(private val dateTimeProvider: DateTimeProvider) {
	fun filter(checkboxListSize: Int, history: List<HabitDay>): List<HabitDay> {
		val daysDisplayed: List<LocalDate> = dateTimeProvider.getCurrentDate().getLastNDays(checkboxListSize.toLong())

		val filteredHistory = history.filter {
			it.day.isAfter(daysDisplayed.first().minusDays(1)) && it.day.isBefore(daysDisplayed.last().plusDays(1))
		}

		if(filteredHistory.size == checkboxListSize) {
			return filteredHistory
		} else {
			val minDate: HabitDay = filteredHistory.stream()
				.min(HabitDay::compareTo)
				.get()

			val maxDate: HabitDay = filteredHistory.stream()
				.max(HabitDay::compareTo)
				.get()


		}

	}
}

(2019-05-25, 2019-05-27, 2019-05-28)
