package com.rwawrzyniak.getby.core.ext.date

import com.rwawrzyniak.getby.habits.DayHeaderDto
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

val LocalDate.toDayHeaderDto: DayHeaderDto
	get() = DayHeaderDto(
		this.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()),
		this.dayOfMonth
	)


fun LocalDate.datesInRangeFromToday(lastDays: Long, futureDays: Long): List<LocalDate> = datesBetweenExcludingStartIncludingEnd(this.minusDays(lastDays), this.plusDays(futureDays))

fun LocalDate.getLastNDays(lastDays: Long): List<LocalDate> = datesBetweenExcludingStartIncludingEnd(this.minusDays(lastDays), this)

fun datesBetweenExcludingStartIncludingEnd(
	start: LocalDate,
	end: LocalDate?
): List<LocalDate> {
	require(start.isBefore(end)){ "Start day must be before end day"}
	val ret: MutableList<LocalDate> = ArrayList()
	var date = start.plusDays(1)
	while (!date.isAfter(end)) {
		ret.add(date)
		date = date.plusDays(1)
	}
	return ret
}

fun LocalDate.toShortForm() = this.format(DateTimeFormatter.ofPattern("ccc d.MM"))

fun LocalDate.toDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())