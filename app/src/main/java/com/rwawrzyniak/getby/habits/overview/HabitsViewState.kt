package com.rwawrzyniak.getby.habits.overview

import androidx.recyclerview.widget.DiffUtil
import java.time.LocalDate

data class HabitsViewState(
	val habitsDiffResult: DiffUtil.DiffResult,
	val isShownArchive: Boolean = false,
	val firstHabitDayHeader: LocalDate
)


