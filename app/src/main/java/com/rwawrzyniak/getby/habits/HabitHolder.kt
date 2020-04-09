package com.rwawrzyniak.getby.habits

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.date.getLastNDays
import java.time.LocalDate

// TODO change findVieweBy id to binding, check if it works with changing Days
class HabitHolder(view: View, private val listener: HabitListener)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

	private lateinit var limitedHabitHistory: List<HabitDay>
	private val daysDisplayed: List<LocalDate>

	interface HabitListener {
		fun onRowClicked(habit: Habit)
		fun onCheckboxClicked(habit: Habit)
	}

	private lateinit var habit: Habit

	private val habitDayLayout: ConstraintLayout = itemView.findViewById(R.id.habitItemLayout)
	private val habitNameView: TextView = itemView.findViewById(R.id.habitName)
	private val checkBoxList: List<CheckBox> = habitDayLayout.children.asIterable().filterIsInstance<CheckBox>()

	init {
		itemView.setOnClickListener(this);
		checkBoxList.forEach{ it.setOnClickListener(this) }
		daysDisplayed = LocalDate.now().getLastNDays(checkBoxList.size.toLong())
	}

	fun bind(habit: Habit) {
		this.habit = habit

		limitedHabitHistory = this.habit.history.filter {
			it.day.isAfter(daysDisplayed.first().minusDays(1)) && it.day.isBefore(daysDisplayed.last())
		}

		habitNameView.text = this.habit.name
		checkBoxList.forEachIndexed {
				index, checkBox -> checkBox.isChecked = limitedHabitHistory[index].checked
		}
	}

	override fun onClick(v: View?) {
		if (v is CheckBox) {
			val dayNumber = limitedHabitHistory[v.tag.toString().toInt()].dayNumber
			habit.history[dayNumber].checked = v.isChecked
			listener.onCheckboxClicked(habit)
		} else {
			listener.onRowClicked(habit)
		}
	}
}
