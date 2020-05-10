package com.rwawrzyniak.getby.habits.ui.createupdate

import com.rwawrzyniak.getby.models.HabitModel

data class HabitCreateUpdateViewState(
	val isUpdateMode: Boolean = false,
	val backingHabit: HabitModel? = null
)

