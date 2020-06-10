package com.rwawrzyniak.getby.habits.ui.overview

import androidx.recyclerview.widget.DiffUtil
import com.rwawrzyniak.getby.models.HabitModel

class HabitDiffCallback(
	private val oldList: List<HabitModel>,
	private val newList: List<HabitModel>
) : DiffUtil.Callback() {

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return newList[newItemPosition].id == oldList[oldItemPosition].id;
	}

	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
		newList[newItemPosition] == oldList[oldItemPosition]
}
