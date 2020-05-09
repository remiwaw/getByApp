package com.rwawrzyniak.getby.habits.ui.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.entities.Habit

// TODO move logic from adapter somwhere
class HabitsAdapter(
	private var habits: MutableList<com.rwawrzyniak.getby.entities.Habit> = mutableListOf(),
	private val onHabitListener: HabitHolder.HabitListener
)
	: RecyclerView.Adapter<HabitHolder>() {

	private var recentlySwipedItemPosition: Int = -1
	private lateinit var recentlySwipedItem: com.rwawrzyniak.getby.entities.Habit

	fun setData(data: List<com.rwawrzyniak.getby.entities.Habit>){
		habits = data.toMutableList()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
		val context: Context = parent.context
		val inflater = LayoutInflater.from(context)

		return HabitHolder(
			inflater.inflate(R.layout.item_habit, parent, false),
			onHabitListener
		)
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

	fun processSwipedItem(position: Int): com.rwawrzyniak.getby.entities.Habit {
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

	companion object{
		private const val ARCHIVED_HABIT = 1
		private const val ACTIVE_HABIT = 0
	}
}
