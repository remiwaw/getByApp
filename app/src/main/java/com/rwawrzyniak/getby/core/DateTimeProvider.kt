package com.rwawrzyniak.getby.core

import java.time.LocalDateTime

class DateTimeProvider {
	private var fixedDatetime: LocalDateTime? = null

	fun getCurrentDateTime() = fixedDatetime ?: LocalDateTime.now()
	fun getCurrentDate() = fixedDatetime?.toLocalDate() ?: LocalDateTime.now().toLocalDate()

	// use only for tests
	fun setCurrentTime(time: LocalDateTime) {
		fixedDatetime = time
	}
}
