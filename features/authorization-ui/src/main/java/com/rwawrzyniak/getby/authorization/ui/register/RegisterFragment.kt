package com.rwawrzyniak.getby.authorization.ui.register

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.rwawrzyniak.getby.authorization.RegisterResult
import com.rwawrzyniak.getby.authorization.ui.R
import com.rwawrzyniak.getby.authorization.ui.databinding.FragmentRegisterBinding
import com.rwawrzyniak.getby.core.android.fragment.BaseFragment
import com.rwawrzyniak.getby.core.android.rx.SchedulerProvider
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment() {
    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: RegisterViewModel by viewModel()
	private val schedulerProvider: SchedulerProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
//        viewModel.registerResultLiveData.observe(viewLifecycleOwner) { registerResult ->
//            when (registerResult) {
//                is RegisterResult.Success -> navigateToDashboard()
//                is RegisterResult.Fail -> handleFailRegistration(registerResult)
//            }
//            binding.registerSignUpButton.hideProgress(R.string.register_button)
//        }
    }

    private fun handleFailRegistration(registerResult: RegisterResult.Fail) {
        binding.registerUserInputLayout.error = registerResult.error
        binding.passwordInputField.setText("")
        binding.passwordInputLayout.isErrorEnabled = true
    }

    private fun navigateToDashboard() {
        findNavController().navigate(R.id.placeholder)
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}
