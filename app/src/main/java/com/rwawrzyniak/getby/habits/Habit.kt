package com.rwawrzyniak.getby.habits

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

@Entity
data class Habit(
	@PrimaryKey val id: String = UUID.randomUUID().toString(),
	@ColumnInfo var name: String,
	@ColumnInfo var description: String,
	@ColumnInfo var frequency: Frequency,
	@ColumnInfo var reminder: Reminder?,
	@ColumnInfo var history: List<HabitDay> = emptyList(),
	@ColumnInfo var isArchived: Boolean = false
)

data class Frequency(val times: Int, val cycle: Int)
data class Reminder(val time: HourMinute, val days: List<DayOfWeek> = emptyList()){
	override fun toString(): String {
		val daysAlarm = days.map { it.getDisplayName(
			TextStyle.NARROW_STANDALONE,
			Locale.getDefault()
		) }.reduce { sum, current -> "$sum/$current" }

		return "$time $daysAlarm"
	}
}
data class HourMinute(val hour: Int, val minutes: Int){
	override fun toString(): String {
		if(hour == 0 && minutes == 0) return ""
		val minutesWithLeadingZeros: String = if (minutes < 10) "0$minutes" else minutes.toString()
		return "$hour:$minutesWithLeadingZeros"
	}
}
data class HabitDay(val day: LocalDate, var checked: Boolean = false) : Comparable<HabitDay> {
	override fun compareTo(other: HabitDay): Int = this.day.compareTo(other.day)
}

data class DayScore(val date: LocalDate, val fulfilledPercentage: Int)

fun Habit.getHabitDaysInCycle(today: LocalDate): List<HabitDay> = this.history.filter {
	it.day.isAfter(today.minusDays(this.frequency.cycle.toLong())) && it.day.isBefore(today.plusDays(1))
}

fun Habit.getStartDate() = this.history.firstOrNull()?.day // We assume list is stored in order TODO: check if its always is the case
