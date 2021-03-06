package com.rwawrzyniak.getby.core.ext

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun LocalDate.datesInRangeFromToday(lastDays: Long, futureDays: Long): List<LocalDate> =
	datesBetweenExcludingStartIncludingEnd(
		this.minusDays(lastDays), this.plusDays(futureDays)
	)

fun LocalDate.getLastNDays(lastDays: Long): List<LocalDate> =
	datesBetweenExcludingStartIncludingEnd(
		this.minusDays(lastDays), this
	)

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

fun LocalDate.toShortForm(): String = this.format(DateTimeFormatter.ofPattern("ccc d.MM"))
fun LocalDate.toddMM(): String = this.format(DateTimeFormatter.ofPattern("dd MMM"))

fun LocalDate.toDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
