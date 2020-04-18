package com.rwawrzyniak.getby.habits.details

import com.github.mikephil.charting.data.Entry

data class HabitDetailsViewState(
	val linearChartEntries: List<Entry>,
	val linearChartShowLastDays: Int = 7
)
