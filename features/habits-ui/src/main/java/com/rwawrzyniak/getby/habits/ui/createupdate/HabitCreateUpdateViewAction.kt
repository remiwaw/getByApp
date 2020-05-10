package com.rwawrzyniak.getby.habits.ui.createupdate

import com.rwawrzyniak.getby.models.HabitModel

sealed class HabitCreateUpdateViewAction {
	internal class LoadHabit(val habitId: String) : HabitCreateUpdateViewAction()
	internal class OnSaveHabitClicked(val habit: HabitModel) : HabitCreateUpdateViewAction()
	internal class OnInputFieldStateChanged(
		val isNameFieldEmpty: Boolean,
		val isDescriptionFieldEmpty: Boolean
	) : HabitCreateUpdateViewAction()
}
