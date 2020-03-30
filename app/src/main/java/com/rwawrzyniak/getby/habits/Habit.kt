package com.rwawrzyniak.getby.habits

import java.time.DayOfWeek
import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val frequency: Frequency? = null,
    val reminder: Reminder? = null,
    val history: List<String> = emptyList()
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
