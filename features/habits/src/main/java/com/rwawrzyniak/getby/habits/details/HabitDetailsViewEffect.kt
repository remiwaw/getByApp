package com.rwawrzyniak.getby.habits.details

sealed class HabitDetailsViewEffect {
	object DoNothing : HabitDetailsViewEffect()
	object OnMenuEditClicked : HabitDetailsViewEffect()
}
