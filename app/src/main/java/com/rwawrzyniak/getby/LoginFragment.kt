package com.rwawrzyniak.getby


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.databinding.FragmentLoginBinding
import com.rwawrzyniak.getby.databinding.FragmentTourBinding
import kotlinx.android.synthetic.main.activity_main.*

class LoginFragment : Fragment() {
	private lateinit var binding: FragmentLoginBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentLoginBinding.inflate(inflater, container, false)

		binding.loginSignUpLink.setOnClickListener {
			nav_host.findNavController().navigate(R.id.placeholder)
		}

		binding.loginSignInButton.setOnClickListener {  }

		return binding.root
	}
}
