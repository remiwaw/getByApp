package com.rwawrzyniak.getby.habits

import androidx.recyclerview.widget.DiffUtil
import kotlin.math.sign

class HabitDiffCallback(
	private val oldList: List<Habit>,
	private val newList: List<Habit>
) : DiffUtil.Callback() {

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return newList[newItemPosition].id == oldList[oldItemPosition].id;
	}

	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
		newList[newItemPosition] == oldList[oldItemPosition]
}
