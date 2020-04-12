package com.rwawrzyniak.getby.habits.createupdate

import com.rwawrzyniak.getby.habits.Habit

data class HabitCreateUpdateViewState(
	val isUpdateMode: Boolean = false,
	val backingHabit: Habit? = null
)

