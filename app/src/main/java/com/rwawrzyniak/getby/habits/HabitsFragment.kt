package com.rwawrzyniak.getby.habits

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

		val callback: ItemTouchHelper.SimpleCallback = createSwipeCallback()
		val itemTouchHelper = ItemTouchHelper(callback)
		itemTouchHelper.attachToRecyclerView(binding.daysListView)

		viewModel.firstDay.observe(viewLifecycleOwner){
			binding.daysHeaderView.initializeDaysHeader(it)
        }

		viewModel.habits.observe(viewLifecycleOwner){
			binding.daysListView.adapter = HabitsAdapter(it)
			(binding.daysListView.adapter as HabitsAdapter).updateHabitList(it)
        }

		binding.daysListView.layoutManager = LinearLayoutManager(requireContext())

		return binding.root
    }

	private fun createSwipeCallback(): ItemTouchHelper.SimpleCallback {
		return object :
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
				when (direction) {
					LEFT -> {
						val position = viewHolder.adapterPosition
						val habitToDelete = (binding.daysListView.adapter as HabitsAdapter).getItemForDelete(position)
						viewModel.removeHabit(habitToDelete)
					}
					RIGHT -> { }
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
							R.color.swipeRight // TODO move color to styles
						)
					).addSwipeRightActionIcon(R.drawable.ic_archive_black)
					.addSwipeLeftBackgroundColor(
						ContextCompat.getColor(
							this@HabitsFragment.requireContext(),
							R.color.swipeLeft // TODO move color to styles
						)
					)
					.addSwipeLeftActionIcon(R.drawable.ic_delete_black)
					.create()
					.decorate()

				super.onChildDraw(
					c,
					recyclerView,
					viewHolder,
					dX,
					dY,
					actionState,
					isCurrentlyActive
				)
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_top_add -> showCreateHabitPopup()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreateHabitPopup(){
        AddNewHabitDialog.show(requireFragmentManager())
    }
}
