package com.rwawrzyniak.getby.habits.ui.overview

import com.rwawrzyniak.getby.entities.Habit

sealed class HabitsViewAction {
	internal object Init: HabitsViewAction()
	internal class OnRemoveHabit(val habit: com.rwawrzyniak.getby.entities.Habit): HabitsViewAction()
	internal class OnSwitchArchiveState(val habit: com.rwawrzyniak.getby.entities.Habit): HabitsViewAction()
	internal class OnUpdateHabit(val habit: com.rwawrzyniak.getby.entities.Habit): HabitsViewAction()
	internal class OnTextFilterChanged(val filterText: String): HabitsViewAction()
	internal class OnShowArchiveChange(val isHideArchived: Boolean): HabitsViewAction()
}
