package com.rwawrzyniak.getby.habits

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.util.UUID

@Entity
data class Habit(
	@PrimaryKey val id: String = UUID.randomUUID().toString(),
	@ColumnInfo var name: String,
	@ColumnInfo var description: String,
	@ColumnInfo var frequency: Frequency,
	@ColumnInfo var reminder: Reminder?,
	@ColumnInfo var history: List<String> = emptyList(),
	@ColumnInfo var isArchived: Boolean = false
) {
    data class Builder(
        var name: String? = null,
        var description: String? = null,
        var frequency: Frequency? = null,
        var reminder: Reminder? = null,
        var history: List<String>? = emptyList()
    ) {

        fun name(name: String) = apply { this.name = name }
        fun description(description: String) = apply { this.description = description }
        fun frequency(frequency: Frequency) = apply { this.frequency = frequency }
        fun reminder(reminder: Reminder?) = apply { this.reminder = reminder }
        fun history(history: List<String>) = apply { this.history = history }
        fun build() = Habit(
			name = requireNotNull(name),
			description = requireNotNull(description),
			frequency = requireNotNull(frequency),
			reminder = reminder,
			history = requireNotNull(history)
        )
    }
}

data class Frequency(val times: Int, val days: Int)
data class Reminder(val time: HourMinute, val days: List<DayOfWeek> = emptyList())
data class HourMinute(val hour: Int, val minutes: Int)

