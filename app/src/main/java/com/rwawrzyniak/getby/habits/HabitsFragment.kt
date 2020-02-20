package com.rwawrzyniak.getby.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.core.ChromeConfiguration
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitsBinding
import kotlinx.android.synthetic.main.fragment_habits.*

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

        viewModel.firstDay.observe(viewLifecycleOwner){
            daysHeaderView.initializeDaysHeader(it)
        }

        viewModel.habits.observe(viewLifecycleOwner){
            daysListView.adapter = HabitsAdapter(it)
        }

        binding.daysListView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_top_add -> showNewDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showNewDialog(){
        Toast.makeText(
            context,
            "Create new Habit",
            Toast.LENGTH_SHORT
        ).show()
    }
}
