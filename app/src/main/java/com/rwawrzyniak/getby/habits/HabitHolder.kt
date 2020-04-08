package com.rwawrzyniak.getby.habits

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R
import java.time.LocalDate

class HabitHolder(view: View, private val clickListener: HabitClickListener)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

	interface HabitClickListener {
		fun onHabitClick(habit: Habit)
	}

	private lateinit var habit: Habit

	private val habitNameView: TextView = itemView.findViewById(R.id.habitName)
	private val firstCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)
	private val secondCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)
	private val thirdCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)
	private val fourthCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)

	init {
		itemView.setOnClickListener(this);
		firstCheckbox.setOnClickListener(this)
		secondCheckbox.setOnClickListener(this)
		thirdCheckbox.setOnClickListener(this)
		fourthCheckbox.setOnClickListener(this)
	}

	fun bind(habit: Habit) {
		this.habit = habit
		habitNameView.text = this.habit.name
	}

	override fun onClick(v: View?) {
		if (v is CheckBox) {
			habit.history
		}
		clickListener.onHabitClick(habit)
	}
}
