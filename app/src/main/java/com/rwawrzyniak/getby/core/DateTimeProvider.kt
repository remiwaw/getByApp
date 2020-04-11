package com.rwawrzyniak.getby.core

import java.time.LocalDateTime
import java.time.ZoneOffset

class DateTimeProvider {
	private var fixedDatetime: LocalDateTime? = null

	fun getCurrentDateTime() = fixedDatetime ?: LocalDateTime.now()
	fun getCurrentDate() = fixedDatetime?.toLocalDate() ?: LocalDateTime.now().toLocalDate()
	fun getCurrentMiliseconds() = getCurrentDateTime().toInstant(ZoneOffset.UTC).toEpochMilli()

	// use only for tests
	fun setCurrentTime(time: LocalDateTime) {
		fixedDatetime = time
	}
}
