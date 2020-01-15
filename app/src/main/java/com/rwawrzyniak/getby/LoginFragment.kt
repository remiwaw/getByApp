package com.rwawrzyniak.getby


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.activity_main.*

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel by fragmentScopedViewModel { injector.loginViewModel }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginSignUpLink.setOnClickListener {
            nav_host.findNavController().navigate(R.id.placeholder)
        }

        binding.loginSignInButton.setOnClickListener { viewModel.injectionTest() }

        return binding.root
    }
}
