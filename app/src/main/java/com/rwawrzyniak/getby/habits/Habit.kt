package com.rwawrzyniak.getby.habits

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
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

data class Frequency(val times: Int, val days: Int)
data class Reminder(val time: HourMinute, val days: List<DayOfWeek> = emptyList()){
	override fun toString(): String {
		return "At $time every: $days"
	}
}
data class HourMinute(val hour: Int, val minutes: Int){
	override fun toString(): String = "$hour:$minutes"
}
data class HabitDay(val day: LocalDate, val dayNumber: Int, var checked: Boolean = false)
