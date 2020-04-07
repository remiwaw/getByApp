package com.rwawrzyniak.getby.habits.details

sealed class HabitDetailsViewEffect {
	internal data class ConfigureFields(
		val habitNameInput: InputFieldState = InputFieldState(),
		val habitDescriptionInput: InputFieldState = InputFieldState()
	) : HabitDetailsViewEffect()
}

data class InputFieldState(
	val isEnabled: Boolean = true,
	val isError: Boolean = false,
	val errorMessage: String = ""
)
