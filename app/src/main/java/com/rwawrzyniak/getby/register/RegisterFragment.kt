package com.rwawrzyniak.getby.register


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentRegisterBinding
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.activity_main.*

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by fragmentScopedViewModel { injector.registerViewModel }
    private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val registerButton = binding.registerSignUpButton

        bindProgressButton(registerButton)

        binding.registerSignUpLink.setOnClickListener { navigateToLoginFragment() }

        registerButton.setOnClickListener {
            viewModel.login(
                binding.registerUserInputField.toString(),
                binding.passwordInputField.toString()
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .doOnSubscribe {
                    registerButton.showProgress {
                        buttonTextRes = R.string.loading
                        progressColor = Color.WHITE
                    }
                }
                .disposeBy(lifecycle.onStop)
                .subscribe()
        }

        handleRegisterResult()
        return binding.root

    }

    private fun handleRegisterResult() {
        viewModel.registerResultLiveData.observe(viewLifecycleOwner) { registerResult ->
            when (registerResult) {
                is RegisterResult.Success -> navigateToDashboard()
                is RegisterResult.Fail -> handleFailRegistration(registerResult)
            }
            binding.registerSignUpButton.hideProgress(R.string.register_button)
        }
    }

    private fun handleFailRegistration(registerResult: RegisterResult.Fail) {
        binding.registerUserInputLayout.error = registerResult.error
        binding.passwordInputField.setText("")
        binding.passwordInputLayout.isErrorEnabled = true
    }

    private fun navigateToDashboard() {
        nav_host.findNavController().navigate(R.id.placeholder)
    }

    private fun navigateToLoginFragment() {
        nav_host.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}
