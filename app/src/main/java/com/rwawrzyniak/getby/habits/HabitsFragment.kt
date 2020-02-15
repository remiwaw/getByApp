package com.rwawrzyniak.getby.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.core.ChromeConfiguration
import com.rwawrzyniak.getby.databinding.FragmentHabitsBinding

class HabitsFragment : BaseFragment() {
    private lateinit var binding: FragmentHabitsBinding

    override fun getChromeConfig(): ChromeConfiguration = ChromeConfiguration(
        showActionBar = true,
        actionBarTitle = getString(R.string.habits_action_bar_title),
        showBottomNavigationBar = true
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitsBinding.inflate(inflater, container, false)

        return binding.root
    }
}
