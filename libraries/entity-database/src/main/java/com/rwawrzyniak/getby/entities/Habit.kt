package com.rwawrzyniak.getby.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Collections.emptyList
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
) : AbstractEntity()

data class Frequency(val times: Int, val cycle: Int)
data class Reminder(val time: HourMinute, val days: List<DayOfWeek> = emptyList())
data class HourMinute(val hour: Int, val minutes: Int)
data class HabitDay(val day: LocalDate, var checked: Boolean = false)
