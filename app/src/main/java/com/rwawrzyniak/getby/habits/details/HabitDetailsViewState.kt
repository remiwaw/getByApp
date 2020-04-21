package com.rwawrzyniak.getby.habits.details

import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.habits.Habit
import java.util.Date

data class HabitDetailsViewState(
	val linearChartEntries: List<Entry>,
	val habit: Habit? = null,
	val habitName: String = "",
	val frequencyText: String = "",
	val habitAlarmText: String = "",
	val historyCalendarState: HistoryCalendarState? = null
)

data class HistoryCalendarState(
	val minDate: Date = Date(0),
	val maxDate: Date,
	val selectedDates: Collection<Date>,
	val highlightedDates: Collection<Date>,
	val isChanged: Boolean = false
)
