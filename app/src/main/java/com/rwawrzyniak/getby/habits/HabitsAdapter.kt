package com.rwawrzyniak.getby.habits

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rwawrzyniak.getby.R

class HabitsAdapter(private var habits: MutableList<Habit>, private var filteredHabits: MutableList<Habit> = arrayListOf())
	: RecyclerView.Adapter<HabitHolder>(), Filterable {

	private var recentlySwipedItemPosition: Int = -1
	private lateinit var recentlySwipedItem: Habit

	init {
	    filteredHabits = habits.filter { !it.isArchived }.toMutableList()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
		val context: Context = parent.context
		val inflater = LayoutInflater.from(context)

		return HabitHolder(inflater.inflate(R.layout.item_habit, parent, false))
	}

	override fun getItemCount(): Int = filteredHabits.size

	override fun onBindViewHolder(holder: HabitHolder, position: Int) {
		if(holder.itemViewType == ARCHIVED_HABIT){
			holder.itemView.apply { setBackgroundColor(context.getColor(R.color.archiveHabit)) }
		}

		val habit = filteredHabits[position]
		holder.bind(habit)
	}

	override fun getItemViewType(position: Int): Int =
		if(filteredHabits[position].isArchived) ARCHIVED_HABIT else ACTIVE_HABIT

	override fun getFilter(): Filter {
		return object : Filter() {

			override fun performFiltering(constraint: CharSequence?): FilterResults {
				val filteResults = FilterResults()
				filteResults.values = doFilter(constraint)
				return filteResults
			}

			private fun doFilter(constraint: CharSequence?): MutableList<Habit> {
				if (constraint.isNullOrEmpty()) {
					filteredHabits = habits
				} else filteredHabits = habits.filter { it.name.contains(constraint, ignoreCase = true) }.toMutableList()

				return filteredHabits
			}

			@Suppress("UNCHECKED_CAST")
			override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
				filteredHabits = (results?.values as List<Habit>).toMutableList()
				notifyDataSetChanged()
			}
		}
	}

	fun processSwipedItem(position: Int): Habit {
		recentlySwipedItem = filteredHabits[position]
		recentlySwipedItemPosition = position
		filteredHabits.remove(recentlySwipedItem)
		notifyItemRemoved(position)
		return recentlySwipedItem
	}

	fun undo() {
		filteredHabits.add(
			recentlySwipedItemPosition,
			recentlySwipedItem
		)
		notifyItemInserted(recentlySwipedItemPosition)
	}

	fun showAllHabits(){
		filteredHabits = habits
		notifyDataSetChanged()
	}

	fun hideArchivedHabits(){
		filteredHabits = filteredHabits.filter { !it.isArchived }.toMutableList()
		notifyDataSetChanged()
	}

	// This really changes the habits also in database
	fun updateHabitListWithDiff(newHabits: List<Habit>){
		habits = newHabits.toMutableList()
		val diffResult = DiffUtil
			.calculateDiff(HabitDiffCallback(filteredHabits, newHabits))
		diffResult.dispatchUpdatesTo(this)
	}

	companion object{
		enum class HabitFilter {SHOW_ALL, HIDE_ARCHIVED}

		private const val ARCHIVED_HABIT = 1
		private const val ACTIVE_HABIT = 0
	}
}
