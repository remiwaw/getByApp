package com.rwawrzyniak.getby.habits.ui.overview

import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.ext.getLastNDays
import com.rwawrzyniak.getby.entities.HabitDay
import java.time.LocalDate
import javax.inject.Inject

// TODO make it simpler
class HabitHolderHelper @Inject constructor(private val dateTimeProvider: DateTimeProvider) {
	fun filter(checkboxListSize: Int, history: List<com.rwawrzyniak.getby.entities.HabitDay>): List<com.rwawrzyniak.getby.entities.HabitDay> {
		val daysDisplayed: List<LocalDate> =
			dateTimeProvider.getCurrentDate().getLastNDays(checkboxListSize.toLong())

		if (history.isEmpty()) {
			return daysDisplayed.map {
				com.rwawrzyniak.getby.entities.HabitDay(
					it
				)
			}
		}

		val filteredHistory: MutableList<com.rwawrzyniak.getby.entities.HabitDay> = history.filter {
			it.day.isAfter(daysDisplayed.first().minusDays(1)) && it.day.isBefore(daysDisplayed.last().plusDays(1))
		}.toMutableList()

		daysDisplayed.forEach {
			if(!filteredHistory.map { it.day }.contains(it)){
				filteredHistory.add(
					com.rwawrzyniak.getby.entities.HabitDay(
						it,
						checked = false
					)
				)
			}
		}

		return filteredHistory
	}
}
