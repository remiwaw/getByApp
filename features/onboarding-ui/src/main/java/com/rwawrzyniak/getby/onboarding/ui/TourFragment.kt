package com.rwawrzyniak.getby.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.authorization.onboarding.ui.R
import com.rwawrzyniak.getby.authorization.onboarding.ui.databinding.FragmentTourBinding
import com.rwawrzyniak.getby.core.android.fragment.BaseFragment

class TourFragment : BaseFragment() {

    private lateinit var binding: FragmentTourBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTourBinding.inflate(inflater, container, false)

        binding.goToNextScreenButton.setOnClickListener {
            findNavController().navigate(R.id.action_tourFragment_to_habitsFragment)
        }

        return binding.root
    }
}
