package com.rwawrzyniak.getby.habits.createupdate

sealed class HabitDetailsViewEffect {
	internal data class ConfigureFields(
		val habitNameInput: InputFieldState = InputFieldState(),
		val habitDescriptionInput: InputFieldState = InputFieldState(),
		val frequencyInput: InputFieldState = InputFieldState()
	) : HabitDetailsViewEffect()
	object GoBack : HabitDetailsViewEffect()
}


data class InputFieldState(
	val isEnabled: Boolean = true,
	val isError: Boolean = false,
	val errorMessage: String = ""
)
