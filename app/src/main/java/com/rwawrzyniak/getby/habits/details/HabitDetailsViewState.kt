package com.rwawrzyniak.getby.habits.details

import com.github.mikephil.charting.data.Entry

data class HabitDetailsViewState(
	val linearChartEntries: List<Entry>,
	val habitName: String = "",
	val frequencyText: String = "",
	val habitAlarmText: String = "",
	val todayEpochDay: Long = 0,
	val linearChartShowLastDays: Int = 7
)
