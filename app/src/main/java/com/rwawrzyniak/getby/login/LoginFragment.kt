package com.rwawrzyniak.getby.login


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
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentLoginBinding
import com.rwawrzyniak.getby.core.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.activity_main.*

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val compositeDisposable = CompositeDisposable()
    private val viewModel by fragmentScopedViewModel { injector.loginViewModel }
    private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val loginSignInButton = binding.loginSignInButton

        bindProgressButton(loginSignInButton)

        binding.loginSignUpLink.setOnClickListener {
            nav_host.findNavController().navigate(R.id.placeholder)
        }

        loginSignInButton.setOnClickListener {
            compositeDisposable.add(
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
            )
        }

        handleLoginResult()

        return binding.root
    }

    private fun handleLoginResult() {
        viewModel.loginResultLiveData.observe(viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                is LoginResult.Success -> navigateToDashboard()
                is LoginResult.Fail -> handleFailedLoginAttempt(loginResult)
            }
            binding.loginSignInButton.hideProgress(R.string.login_button_sign_in)
        }
    }

    private fun handleFailedLoginAttempt(loginResult: LoginResult.Fail) {
        binding.loginInputLayout.error = loginResult.error
        binding.passwordInputField.setText("")
        binding.passwordInputLayout.isErrorEnabled = true
    }

    private fun navigateToDashboard() {
        nav_host.findNavController().navigate(R.id.placeholder)
    }
}
