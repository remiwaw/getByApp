package com.rwawrzyniak.getby.habits

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R

class HabitsAdapter(private val habits: MutableList<Habit>)
    : RecyclerView.Adapter<HabitHolder>() {

	private var recentlySwipedItemPosition: Int = -1
	private lateinit var recentlySwipedItem: Habit

	init {
	    habits.filter { !it.isArchived }
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        return HabitHolder(inflater.inflate(R.layout.item_habit, parent, false))
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitHolder, position: Int) {
		if(holder.itemViewType == ARCHIVED_HABIT){
			holder.itemView.apply { setBackgroundColor(context.getColor(R.color.archiveHabit)) }
		}

		val habit = habits[position]
		holder.bind(habit)
    }

	override fun getItemViewType(position: Int): Int =
		if(habits[position].isArchived) ARCHIVED_HABIT else ACTIVE_HABIT

	fun processSwipedItem(position: Int): Habit {
		recentlySwipedItem = habits[position]
		recentlySwipedItemPosition = position
		habits.remove(recentlySwipedItem)
		notifyItemRemoved(position)
		return recentlySwipedItem
	}

	fun undo() {
		habits.add(
			recentlySwipedItemPosition,
			recentlySwipedItem
		)
		notifyItemInserted(recentlySwipedItemPosition)
	}

	fun updateHabitList(newHabits: List<Habit>){
		val diffResult = DiffUtil
			.calculateDiff(HabitDiffCallback(habits, newHabits))
		diffResult.dispatchUpdatesTo(this)
	}

	companion object{
		private const val ARCHIVED_HABIT = 1
		private const val ACTIVE_HABIT = 0
	}
}

