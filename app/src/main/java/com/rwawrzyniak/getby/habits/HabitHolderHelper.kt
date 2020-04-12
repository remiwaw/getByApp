package com.rwawrzyniak.getby.habits

import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.ext.date.getLastNDays
import java.time.LocalDate
import javax.inject.Inject

class HabitHolderHelper @Inject constructor(private val dateTimeProvider: DateTimeProvider) {
	fun filter(checkboxListSize: Int, history: List<HabitDay>): List<HabitDay> {
		val daysDisplayed: List<LocalDate> = dateTimeProvider.getCurrentDate().getLastNDays(checkboxListSize.toLong())

		return history.filter {
			it.day.isAfter(daysDisplayed.first().minusDays(1)) && it.day.isBefore(daysDisplayed.last())
		}
	}
}

