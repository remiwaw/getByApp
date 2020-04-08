package com.rwawrzyniak.getby.date

import com.rwawrzyniak.getby.habits.DayHeaderDto
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

val LocalDate.toDayHeaderDto: DayHeaderDto
	get() = DayHeaderDto(
		this.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()),
		this.dayOfMonth
	)


fun LocalDate.getLastXDays(lastDays: Long): List<LocalDate> = datesBetween(this, this.minusDays(lastDays))

private fun datesBetween(
	start: LocalDate,
	end: LocalDate?
): List<LocalDate> {
	val ret: MutableList<LocalDate> = ArrayList()
	var date = start
	while (!date.isAfter(end)) {
		ret.add(date)
		date = date.plusDays(1)
	}
	return ret
}
