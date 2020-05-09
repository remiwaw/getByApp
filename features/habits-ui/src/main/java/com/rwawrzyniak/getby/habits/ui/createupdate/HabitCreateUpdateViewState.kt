package com.rwawrzyniak.getby.habits.ui.createupdate

import com.rwawrzyniak.getby.entities.Habit

data class HabitCreateUpdateViewState(
	val isUpdateMode: Boolean = false,
	val backingHabit: com.rwawrzyniak.getby.entities.Habit? = null
)

