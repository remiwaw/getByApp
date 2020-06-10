package com.rwawrzyniak.getby.habits.ui.overview

import com.rwawrzyniak.getby.models.HabitModel

sealed class HabitsViewAction {
	internal object Init: HabitsViewAction()
	internal class OnRemoveHabit(val habit: HabitModel): HabitsViewAction()
	internal class OnSwitchArchiveState(val habit: HabitModel): HabitsViewAction()
	internal class OnUpdateHabit(val habit: HabitModel): HabitsViewAction()
	internal class OnTextFilterChanged(val filterText: String): HabitsViewAction()
	internal class OnShowArchiveChange(val isHideArchived: Boolean): HabitsViewAction()
}
