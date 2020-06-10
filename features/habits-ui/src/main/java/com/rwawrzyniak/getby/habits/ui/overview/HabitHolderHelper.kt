package com.rwawrzyniak.getby.habits.ui.overview

import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.ext.getLastNDays
import com.rwawrzyniak.getby.models.HabitDay
import java.time.LocalDate

// TODO make it simpler
class HabitHolderHelper constructor(private val dateTimeProvider: DateTimeProvider) {
	fun filter(checkboxListSize: Int, history: List<HabitDay>): List<HabitDay> {
		val daysDisplayed: List<LocalDate> =
			dateTimeProvider.getCurrentDate().getLastNDays(checkboxListSize.toLong())

		if (history.isEmpty()) {
			return daysDisplayed.map { HabitDay(it) }
		}

		val filteredHistory: MutableList<HabitDay> = history.filter {
			it.day.isAfter(daysDisplayed.first().minusDays(1)) && it.day.isBefore(daysDisplayed.last().plusDays(1))
		}.toMutableList()

		daysDisplayed.forEach { dayDisplayed ->
			if(!filteredHistory.map { it.day }.contains(dayDisplayed)){
				filteredHistory.add(
					HabitDay(
						dayDisplayed,
						checked = false
					)
				)
			}
		}

		return filteredHistory
	}
}
