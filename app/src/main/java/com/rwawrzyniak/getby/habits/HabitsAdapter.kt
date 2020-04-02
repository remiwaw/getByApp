package com.rwawrzyniak.getby.habits

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R

class HabitsAdapter(private val habits: MutableList<Habit>)
    : RecyclerView.Adapter<HabitHolder>() {

	private var recentlyDeletedItemPosition: Int = -1
	private lateinit var recentlyDeletedItem: Habit

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        return HabitHolder(inflater.inflate(R.layout.item_habit, parent, false))
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitHolder, position: Int) {
        val habit = habits[position]
        holder.bind(habit)
    }

	fun getItemForDelete(position: Int): Habit {
		recentlyDeletedItem = habits[position]
		recentlyDeletedItemPosition = position
		habits.remove(recentlyDeletedItem)
		notifyItemRemoved(position)
		return recentlyDeletedItem
	}

	fun undoDelete() {
		habits.add(
			recentlyDeletedItemPosition,
			recentlyDeletedItem
		)
		notifyItemInserted(recentlyDeletedItemPosition)
	}

	fun updateHabitList(newHabits: List<Habit>){
		val diffResult = DiffUtil
			.calculateDiff(HabitDiffCallback(habits, newHabits))
		diffResult.dispatchUpdatesTo(this)
	}
}

