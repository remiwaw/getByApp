package com.rwawrzyniak.getby.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.android.fragment.BaseFragment
import com.rwawrzyniak.getby.databinding.FragmentOnboardingBinding
import kotlinx.android.synthetic.main.activity_main.*

class OnboardingFragment : BaseFragment() {

    private val sampleImages = intArrayOf(
        R.drawable.onboarding_one,
        R.drawable.onboarding_two,
        R.drawable.onboarding_three
    )

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        binding.carouselView.pageCount = sampleImages.size
        binding.carouselView.setImageListener { position, imageView ->
            imageView.setImageResource(sampleImages[position])
        }

        binding.skipButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }

        return binding.root
    }
}
