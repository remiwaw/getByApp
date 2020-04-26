package com.rwawrzyniak.getby.habits.overview

import com.rwawrzyniak.getby.habits.persistance.Habit

sealed class HabitsViewAction {
	internal object Init: HabitsViewAction()
	internal class OnRemoveHabit(val habit: Habit): HabitsViewAction()
	internal class OnArchiveHabit(val habit: Habit): HabitsViewAction()
	internal class OnUpdateHabit(val habit: Habit): HabitsViewAction()
	internal class OnTextFilterChanged(val filterText: String): HabitsViewAction()
	internal class OnShowArchiveChange(val isHideArchived: Boolean): HabitsViewAction()
}
