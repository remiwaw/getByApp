package com.rwawrzyniak.getby.authorization.ui.login

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.authorization.LoginResult
import com.rwawrzyniak.getby.authorization.ui.R
import com.rwawrzyniak.getby.core.android.ext.fragmentScopedViewModel
import com.rwawrzyniak.getby.core.android.fragment.BaseFragment
import com.rwawrzyniak.getby.core.android.rx.SchedulerProvider
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentLoginBinding
import io.sellmair.disposer.onStop

class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel by fragmentScopedViewModel { injector.loginViewModel }
    private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val loginSignInButton = binding.loginSignInButton

        bindProgressButton(loginSignInButton)

        binding.loginSignUpLink.setOnClickListener { navigateToRegisterFragment() }

        loginSignInButton.setOnClickListener {
            viewModel.login(
                binding.loginInputField.toString(),
                binding.passwordInputField.toString()
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .doOnSubscribe {
                    loginSignInButton.showProgress {
                        buttonTextRes = R.string.loading
                        progressColor = Color.WHITE
                    }
                }
                .disposeBy(lifecycle.onStop)
                .subscribe()
        }

        handleLoginResult()

        return binding.root
    }

    private fun handleLoginResult() {
        viewModel.loginResultLiveData.observe(viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                is LoginResult.Success -> navigateToHabits()
//                is LoginResult.Fail -> handleFailedLoginAttempt(loginResult)
                is LoginResult.Fail -> navigateToHabits()
            }
            binding.loginSignInButton.hideProgress(R.string.login_button)
        }
    }

    private fun handleFailedLoginAttempt(loginResult: LoginResult.Fail) {
        binding.loginInputLayout.error = loginResult.error
        binding.passwordInputField.setText("")
        binding.passwordInputLayout.isErrorEnabled = true
    }

    private fun navigateToHabits() {
        findNavController().navigate(R.id.action_loginFragment_to_habitsFragment)
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
}
