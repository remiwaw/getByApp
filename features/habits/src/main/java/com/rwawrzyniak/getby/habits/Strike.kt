package com.rwawrzyniak.getby.habits

import java.time.LocalDate

class Strike(val startDate: LocalDate, val endDate: LocalDate, val daysInRow: Int){
	override fun equals(other: Any?): Boolean {
		val otherStrike = other as Strike
		return startDate.isEqual(otherStrike.startDate) &&
		endDate.isEqual(otherStrike.endDate) &&
		daysInRow == other.daysInRow
	}
}
