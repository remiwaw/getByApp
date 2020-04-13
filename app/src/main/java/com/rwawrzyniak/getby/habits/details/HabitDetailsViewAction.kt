package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.habits.Habit

sealed class HabitDetailsViewAction {
	internal class LoadHabit(val habitId: String) : HabitDetailsViewAction()
	internal class OnSaveHabitClicked(val habit: Habit) : HabitDetailsViewAction()
	internal class OnInputFieldStateChanged(
		val isNameFieldEmpty: Boolean,
		val isDescriptionFieldEmpty: Boolean
	) : HabitDetailsViewAction()
}
