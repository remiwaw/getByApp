package com.rwawrzyniak.getby.habits.details

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.habits.persistance.Habit
import java.util.Date

data class HabitDetailsViewState(
	val linearChartEntries: List<Entry>,
	val bestStrikeLineEntries: List<BarEntry> = emptyList(),
	val historyCalendarState: HistoryCalendarState? = null,
	val habitName: String = "",
	val frequencyText: String = "",
	val habitAlarmText: String = "",
	val habit: Habit? = null
)

data class HistoryCalendarState(
	val minDate: Date = Date(0),
	val maxDate: Date,
	val selectedDates: Collection<Date>,
	val highlightedDates: Collection<Date>,
	val isSelectedDatesChanged: Boolean = false
)
