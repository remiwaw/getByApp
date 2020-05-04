package com.rwawrzyniak.getby.habits.overview

import androidx.recyclerview.widget.DiffUtil
import com.rwawrzyniak.getby.habits.persistance.Habit
import java.time.LocalDate

data class HabitsViewState(
	val updatedHabitsInfo: UpdatedHabitsInfo,
	val isHideArchive: Boolean = true,
	val isInit: Boolean = false, // only in initialize phase we change some views, i.e isShowArchive
	val firstHabitDayHeader: LocalDate
)

data class UpdatedHabitsInfo(val updatedList: List<Habit>, val habitsDiffResult: DiffUtil.DiffResult)


