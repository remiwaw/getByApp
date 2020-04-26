package com.rwawrzyniak.getby.habits.overview

import androidx.recyclerview.widget.DiffUtil
import com.rwawrzyniak.getby.habits.persistance.Habit
import java.time.LocalDate

data class HabitsViewState(
	val updatedHabitsInfo: UpdatedHabitsInfo,
	val isShownArchive: Boolean = false,
	val firstHabitDayHeader: LocalDate
)

data class UpdatedHabitsInfo(val updatedList: List<Habit>, val habitsDiffResult: DiffUtil.DiffResult)


