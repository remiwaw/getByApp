package com.rwawrzyniak.getby.habits.ui.overview

import androidx.recyclerview.widget.DiffUtil
import com.rwawrzyniak.getby.models.HabitModel
import java.time.LocalDate

data class HabitsViewState(
	val updatedHabitsInfo: UpdatedHabitsInfo,
	val isHideArchive: Boolean = true,
	val isInit: Boolean = false, // only in initialize phase we change some views, i.e isShowArchive
	val firstHabitDayHeader: LocalDate
)

data class UpdatedHabitsInfo(val updatedList: List<HabitModel>, val habitsDiffResult: DiffUtil.DiffResult)


