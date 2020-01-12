package com.rwawrzyniak.getby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.rwawrzyniak.getby.databinding.FragmentOnboardingBinding
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.fragment_tour.*


class OnboardingFragment : Fragment() {

    private var sampleImages = intArrayOf(
        R.drawable.onboarding_one,
        R.drawable.onboarding_two,
        R.drawable.onboarding_three
    )

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        binding.carouselView.pageCount = sampleImages.size
        binding.carouselView.setImageListener { position, imageView ->
            imageView.setImageResource(sampleImages[position])
        }

        return binding.root
    }
}
