package com.rwawrzyniak.getby.habits.createupdate

import com.rwawrzyniak.getby.habits.Habit

sealed class HabitCreateUpdateViewAction {
	internal class LoadHabit(val habitId: String) : HabitCreateUpdateViewAction()
	internal class OnSaveHabitClicked(val habit: Habit) : HabitCreateUpdateViewAction()
	internal class OnInputFieldStateChanged(
		val isNameFieldEmpty: Boolean,
		val isDescriptionFieldEmpty: Boolean
	) : HabitCreateUpdateViewAction()
}
