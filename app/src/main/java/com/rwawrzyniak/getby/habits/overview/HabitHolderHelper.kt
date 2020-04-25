package com.rwawrzyniak.getby.habits.overview

import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.ext.date.getLastNDays
import com.rwawrzyniak.getby.habits.persistance.HabitDay
import java.time.LocalDate
import javax.inject.Inject

// TODO make it simpler
class HabitHolderHelper @Inject constructor(private val dateTimeProvider: DateTimeProvider) {
	fun filter(checkboxListSize: Int, history: List<HabitDay>): List<HabitDay> {
		val daysDisplayed: List<LocalDate> =
			dateTimeProvider.getCurrentDate().getLastNDays(checkboxListSize.toLong())

		if (history.isEmpty()) {
			return daysDisplayed.map {
				HabitDay(
					it
				)
			}
		}

		val filteredHistory: MutableList<HabitDay> = history.filter {
			it.day.isAfter(daysDisplayed.first().minusDays(1)) && it.day.isBefore(daysDisplayed.last().plusDays(1))
		}.toMutableList()

		daysDisplayed.forEach {
			if(!filteredHistory.map { it.day }.contains(it)){
				filteredHistory.add(
					HabitDay(
						it,
						checked = false
					)
				)
			}
		}

		return filteredHistory
	}
}
