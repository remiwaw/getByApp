package com.rwawrzyniak.getby.habits.ui.createupdate

import com.rwawrzyniak.getby.entities.Habit

sealed class HabitCreateUpdateViewAction {
	internal class LoadHabit(val habitId: String) : HabitCreateUpdateViewAction()
	internal class OnSaveHabitClicked(val habit: com.rwawrzyniak.getby.entities.Habit) : HabitCreateUpdateViewAction()
	internal class OnInputFieldStateChanged(
		val isNameFieldEmpty: Boolean,
		val isDescriptionFieldEmpty: Boolean
	) : HabitCreateUpdateViewAction()
}
