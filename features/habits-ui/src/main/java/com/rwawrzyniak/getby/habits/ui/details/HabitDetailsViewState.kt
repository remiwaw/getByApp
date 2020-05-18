package com.rwawrzyniak.getby.habits.ui.details

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.models.HabitModel
import java.util.Date

data class HabitDetailsViewState(
	val linearChartEntries: List<Entry>,
	val bestStrikeLineEntries: List<BarEntry> = emptyList(),
	val historyCalendarState: HistoryCalendarState? = null,
	val habitName: String = "",
	val frequencyText: String = "",
	val habitAlarmText: String = "",
	val habit: HabitModel? = null
)

data class HistoryCalendarState(
	val minDate: Date = Date(0),
	val maxDate: Date,
	val selectedDates: Collection<Date> = emptyList(),
	val highlightedDates: Collection<Date>,
	val isSelectedDatesChanged: Boolean = false
)
