package com.rwawrzyniak.getby.habits.overview

import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.core.ChromeConfiguration
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitsBinding
import com.rwawrzyniak.getby.habits.details.HabitDetailsFragment.Companion.ARG_HABIT_ID
import com.rwawrzyniak.getby.habits.persistance.Habit
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*

class HabitsFragment : BaseFragment() {
	private lateinit var binding: FragmentHabitsBinding
	private val viewModel by fragmentScopedViewModel { injector.habitsViewModelImpl }
	private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }

	private val onHabitListener = object:
		HabitHolder.HabitListener {
		override fun onRowClicked(habit: Habit) {
			nav_host.findNavController().navigate(
				R.id.action_habitsFragment_to_habitDetailsFragment,
				Bundle().apply { putString(ARG_HABIT_ID, habit.id) }
			)
		}

		override fun onCheckboxClicked(habit: Habit) {
			simplySubscribe( viewModel.onAction(HabitsViewAction.OnUpdateHabit(habit)) )
		}
	}

	override fun onStart() {
		super.onStart()
		wireViewModel()
	}

	private fun wireViewModel() {
		viewModel.observeState()
			.observeOn(schedulerProvider.main())
			.subscribeOn(schedulerProvider.io())
			.subscribeBy(onNext = this::renderState)
			.disposeBy(lifecycle.onStop)
	}

	private fun renderState(state: HabitsViewState) {
		with(state){
			binding.daysListView.adapter =
				HabitsAdapter(
					habits = viewModel.oldFilteredHabits,
					onHabitListener = onHabitListener
				)
			binding.daysListView.layoutManager = LinearLayoutManager(requireContext())

			binding.daysHeaderView.initializeDaysHeader(firstHabitDayHeader)
			state.habitsDiffResult.dispatchUpdatesTo(binding.daysListView.adapter as HabitsAdapter)
		}
	}

	// diffResult.dispatchUpdatesTo(binding.daysListView.adapter as HabitsAdapter)

	override fun getChromeConfig(): ChromeConfiguration = ChromeConfiguration(
		showActionBar = true,
		actionBarTitle = getString(R.string.habits_action_bar_title),
		showActionBarAddButton = true,
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

		val itemTouchHelper = ItemTouchHelper(HabitsSimpleCallback())
		itemTouchHelper.attachToRecyclerView(binding.daysListView)

		binding.habitSearch.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
				simplySubscribe(viewModel.onAction(HabitsViewAction.OnTextFilterChanged(text.toString())))
			}
		})

		return binding.root
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.top_add -> navigateToCreateHabit()
			R.id.hideArchived -> {
				val isShowArchived: Boolean

					if (item.isChecked) {
						item.isChecked = false
						isShowArchived = true
					} else {
						item.isChecked = true
						isShowArchived = false
					}

				simplySubscribe( viewModel.onAction(HabitsViewAction.OnShowArchiveChange(isShowArchived)))

			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun navigateToCreateHabit() {
		nav_host.findNavController().navigate(R.id.action_habitsFragment_to_habitCreateUpdateFragment)
	}

	private fun showUndoSnackbar(swipedHabit: Habit, swipedAction: SwipedAction) {
		val snackbar = Snackbar.make(
			requireView(),
			resources.getString(
				R.string.habits_undo_snackbar_title,
				swipedAction.name.toLowerCase()
			),
			Snackbar.LENGTH_LONG
		)
		snackbar.setAction(R.string.habits_undo_snackbar_button) { }

		snackbar.addCallback(object : Snackbar.Callback() {
			override fun onDismissed(snackbar: Snackbar, event: Int) {
				if (event == DISMISS_EVENT_ACTION) {
					(binding.daysListView.adapter as HabitsAdapter).undo()
				} else {
						if (swipedAction == SwipedAction.REMOVE) {
							simplySubscribe( viewModel.onAction(HabitsViewAction.OnRemoveHabit(swipedHabit)))
						} else {
							simplySubscribe( viewModel.onAction(HabitsViewAction.OnArchiveHabit(swipedHabit)))
						}
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
		): Boolean = false

		override fun onSwiped(
			viewHolder: RecyclerView.ViewHolder,
			direction: Int
		) {
			val position = viewHolder.adapterPosition
			val swipedItem =
				(binding.daysListView.adapter as HabitsAdapter).processSwipedItem(position)

			when (direction) {
				ItemTouchHelper.LEFT -> showUndoSnackbar(swipedItem,
					SwipedAction.REMOVE
				)
				ItemTouchHelper.RIGHT -> showUndoSnackbar(swipedItem,
					SwipedAction.ARCHIVE
				)
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

	private fun simplySubscribe(completable: Completable) {
		completable.subscribeOn(schedulerProvider.io())
			.observeOn(schedulerProvider.main())
			.subscribe()
			.disposeBy(onStop)
	}

	companion object{
		private enum class SwipedAction {ARCHIVE, REMOVE}
	}
}

