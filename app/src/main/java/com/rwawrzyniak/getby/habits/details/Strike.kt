package com.rwawrzyniak.getby.habits.details

import java.time.LocalDate

class Strike(val startDate: LocalDate, val endDate: LocalDate, val strike: Int){
	override fun equals(other: Any?): Boolean {
		val otherStrike = other as Strike
		return startDate.isEqual(otherStrike.startDate) &&
		endDate.isEqual(otherStrike.endDate) &&
		strike == other.strike
	}
}
