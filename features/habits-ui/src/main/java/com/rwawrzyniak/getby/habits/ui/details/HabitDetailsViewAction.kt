package com.rwawrzyniak.getby.habits.ui.details

import java.util.Date

sealed class HabitDetailsViewAction {
	internal class InitializeView(val habitId: String) : HabitDetailsViewAction()
	internal class LowestVisibleXBecomesVisible(val firstVisibleEpochDay: Int) : HabitDetailsViewAction()
	internal class OnSaveCalendarClicked(val selectedDates: List<Date>): HabitDetailsViewAction()
}
