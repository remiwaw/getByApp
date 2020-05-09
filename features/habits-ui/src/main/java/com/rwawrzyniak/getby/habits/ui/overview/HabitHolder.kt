package com.rwawrzyniak.getby.habits.ui.overview

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.habits.R
import com.rwawrzyniak.getby.entities.Habit
import com.rwawrzyniak.getby.entities.HabitDay

// TODO change findVieweBy id to binding, check if it works with changing Days
class HabitHolder(view: View, private val listener: HabitListener)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

	private val habitHolderHelper: HabitHolderHelper =
		HabitHolderHelper(
			DateTimeProvider()
		)
	private lateinit var limitedHabitHistory: List<com.rwawrzyniak.getby.entities.HabitDay>

	interface HabitListener {
		fun onRowClicked(habit: com.rwawrzyniak.getby.entities.Habit)
		fun onCheckboxClicked(habit: com.rwawrzyniak.getby.entities.Habit)
	}

	private lateinit var habit: com.rwawrzyniak.getby.entities.Habit

	private val habitDayLayout: ConstraintLayout = itemView.findViewById(R.id.habitItemLayout)
	private val habitNameView: TextView = itemView.findViewById(R.id.habitName)
	private val checkBoxList: List<CheckBox> = habitDayLayout.children.asIterable().filterIsInstance<CheckBox>()

	init {
		itemView.setOnClickListener(this)
		checkBoxList.forEach{ it.setOnClickListener(this) }
	}

	fun bind(habit: com.rwawrzyniak.getby.entities.Habit) {
		this.habit = habit
		habitNameView.text = this.habit.name


		limitedHabitHistory = habitHolderHelper.filter(checkBoxList.size, habit.history)
		if(habit.history.isEmpty()){
			habit.history = limitedHabitHistory
		}

		checkBoxList.forEachIndexed {
				index, checkBox -> checkBox.isChecked = limitedHabitHistory.getOrNull(index)?.checked ?: false
		}
	}

	override fun onClick(v: View?) {
		if (v is CheckBox) {
			val chosenDate = limitedHabitHistory[v.tag.toString().toInt()]
			habit.history.first { it.day == chosenDate.day }.checked = v.isChecked
			listener.onCheckboxClicked(habit)
		} else {
			listener.onRowClicked(habit)
		}
	}
}
