package com.rwawrzyniak.getby.habits

import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.core.ChromeConfiguration
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitsBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class HabitsFragment : BaseFragment() {
	private lateinit var binding: FragmentHabitsBinding
	private val viewModel by fragmentScopedViewModel { injector.habitsViewModel }

	override fun getChromeConfig(): ChromeConfiguration = ChromeConfiguration(
		showActionBar = true,
		actionBarTitle = getString(R.string.habits_action_bar_title),
		showBottomNavigationBar = true
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentHabitsBinding.inflate(inflater, container, false)

		val callback: ItemTouchHelper.SimpleCallback = HabitsSimpleCallback()
		val itemTouchHelper = ItemTouchHelper(callback)
		itemTouchHelper.attachToRecyclerView(binding.daysListView)

		binding.habitSearch.addTextChangedListener(object : TextWatcher{
			override fun afterTextChanged(s: Editable?) {}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
				(binding.daysListView.adapter as HabitsAdapter).filter.filter(text)
			}
		})
		viewModel.firstDay.observe(viewLifecycleOwner) {
			binding.daysHeaderView.initializeDaysHeader(it)
		}

		viewModel.habits.observe(viewLifecycleOwner) {
			if(binding.daysListView.adapter == null){
				binding.daysListView.adapter = HabitsAdapter(it)
			}
			(binding.daysListView.adapter as HabitsAdapter).updateHabitListWithDiff(it)
		}

		binding.daysListView.layoutManager = LinearLayoutManager(requireContext())

		return binding.root
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val habitsAdapter = binding.daysListView.adapter as HabitsAdapter
		when (item.itemId) {
			R.id.menu_top_add -> showCreateHabitPopup()
			R.id.hideArchived -> {
				if (item.isChecked) {
					item.isChecked = false;
					habitsAdapter.showAllHabits()
				}
				else {
					item.isChecked = true;
					habitsAdapter.hideArchivedHabits()
				}
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun showCreateHabitPopup() {
		AddNewHabitDialog.show(requireFragmentManager())
	}

	private fun showUndoSnackbar(swipedHabit: Habit, swipedAction: SwipedAction) {
		val snackbar = Snackbar.make(
			requireView(),
			resources.getString(R.string.habits_undo_snackbar_title, swipedAction.name.toLowerCase()),
			Snackbar.LENGTH_LONG
		)
		snackbar.setAction(R.string.habits_undo_snackbar_button) { }

		snackbar.addCallback(object : Snackbar.Callback() {
			override fun onDismissed(snackbar: Snackbar, event: Int) {
				if (event == DISMISS_EVENT_ACTION) {
					(binding.daysListView.adapter as HabitsAdapter).undo()
				} else {
					if(swipedAction == SwipedAction.REMOVE) viewModel.removeHabit(swipedHabit)
					else viewModel.archiveHabit(swipedHabit)
				}
			}

			override fun onShown(snackbar: Snackbar) {}
		})

		snackbar.show()
	}

	private inner class HabitsSimpleCallback :
		ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
		override fun onMove(
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			target: RecyclerView.ViewHolder
		): Boolean {
			return false
		}

		override fun onSwiped(
			viewHolder: RecyclerView.ViewHolder,
			direction: Int
		) {
			val position = viewHolder.adapterPosition
			val swipedItem =
				(binding.daysListView.adapter as HabitsAdapter).processSwipedItem(position)

			when (direction) {
				ItemTouchHelper.LEFT -> showUndoSnackbar(swipedItem, SwipedAction.REMOVE)
				ItemTouchHelper.RIGHT -> showUndoSnackbar(swipedItem, SwipedAction.ARCHIVE)
			}
		}

		override fun onChildDraw(
			c: Canvas,
			recyclerView: RecyclerView,
			viewHolder: RecyclerView.ViewHolder,
			dX: Float,
			dY: Float,
			actionState: Int,
			isCurrentlyActive: Boolean
		) {
			RecyclerViewSwipeDecorator.Builder(
				c,
				recyclerView,
				viewHolder,
				dX,
				dY,
				actionState,
				isCurrentlyActive
			)
				.addSwipeRightBackgroundColor(
					ContextCompat.getColor(
						this@HabitsFragment.requireContext(),
						R.color.swipeRight
					)
				).addSwipeRightActionIcon(R.drawable.ic_archive_black)
				.addSwipeLeftBackgroundColor(
					ContextCompat.getColor(
						this@HabitsFragment.requireContext(),
						R.color.swipeLeft
					)
				)
				.addSwipeLeftActionIcon(R.drawable.ic_delete_black)
				.create()
				.decorate()

			super.onChildDraw(
				c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
			)
		}
	}

	companion object{
		private enum class SwipedAction {ARCHIVE, REMOVE}
	}
}

