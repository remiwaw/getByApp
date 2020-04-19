package com.rwawrzyniak.getby.habits.details

import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.habits.Habit

data class HabitDetailsViewState(
	val linearChartEntries: List<Entry>,
	val habit: Habit? = null,
	val habitName: String = "",
	val frequencyText: String = "",
	val habitAlarmText: String = ""
)
