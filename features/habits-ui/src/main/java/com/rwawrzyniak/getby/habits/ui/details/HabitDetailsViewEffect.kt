package com.rwawrzyniak.getby.habits.ui.details

sealed class HabitDetailsViewEffect {
	object DoNothing : HabitDetailsViewEffect()
	object OnMenuEditClicked : HabitDetailsViewEffect()
}
