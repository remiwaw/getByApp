package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.habits.Habit

data class HabitDetailsViewState(
	val isUpdateMode: Boolean = false,
	val backingHabit: Habit? = null
)

