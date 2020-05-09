package com.rwawrzyniak.getby.habits.ui.overview

import androidx.recyclerview.widget.DiffUtil
import com.rwawrzyniak.getby.entities.Habit
import java.time.LocalDate

data class HabitsViewState(
	val updatedHabitsInfo: UpdatedHabitsInfo,
	val isHideArchive: Boolean = true,
	val isInit: Boolean = false, // only in initialize phase we change some views, i.e isShowArchive
	val firstHabitDayHeader: LocalDate
)

data class UpdatedHabitsInfo(val updatedList: List<com.rwawrzyniak.getby.entities.Habit>, val habitsDiffResult: DiffUtil.DiffResult)


