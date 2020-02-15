package com.rwawrzyniak.getby.tour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.databinding.FragmentTourBinding
import kotlinx.android.synthetic.main.activity_main.*

class TourFragment : BaseFragment() {

    private lateinit var binding: FragmentTourBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTourBinding.inflate(inflater, container, false)

        binding.goToNextScreenButton.setOnClickListener {
            nav_host.findNavController().navigate(R.id.action_tourFragment_to_onboardingFragment)
        }

        return binding.root
    }
}
