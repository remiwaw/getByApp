package com.rwawrzyniak.getby.habits

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.date.getLastNDaysIncludingToday
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

	private val habitNameView: TextView = itemView.findViewById(R.id.habitName)
	private val firstCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)
	private val secondCheckbox: CheckBox = itemView.findViewById(R.id.secondCheckbox)
	private val thirdCheckbox: CheckBox = itemView.findViewById(R.id.thirdCheckbox)
	private val fourthCheckbox: CheckBox = itemView.findViewById(R.id.fourthCheckbox)
	private val checkBoxList = listOf(firstCheckbox, secondCheckbox, thirdCheckbox, fourthCheckbox)

	init {
		itemView.setOnClickListener(this);
		checkBoxList.forEach{ it.setOnClickListener(this) }
		daysDisplayed = LocalDate.now().getLastNDaysIncludingToday(4)
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
			val dayNumber = limitedHabitHistory[v.tag.toString().toInt()-1].dayNumber
			habit.history[dayNumber].checked = v.isChecked
			listener.onCheckboxClicked(habit)
		} else {
			listener.onRowClicked(habit)
		}
	}
}
