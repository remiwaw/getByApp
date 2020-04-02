package com.rwawrzyniak.getby.habits

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R

class HabitHolder(view: View)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    private lateinit var habit: Habit
	private var isMarkedForRemoval: Boolean = false

    private val habitNameView: TextView = itemView.findViewById(R.id.habitName)
    private val firstCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)
    private val secondCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)
    private val thirdCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)
    private val fourthCheckbox: CheckBox = itemView.findViewById(R.id.firstCheckbox)

    fun bind(habit: Habit) {
        this.habit = habit
        habitNameView.text = this.habit.name
    }

    override fun onClick(v: View?) {
    }
}
