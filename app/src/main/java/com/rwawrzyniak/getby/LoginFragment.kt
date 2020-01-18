package com.rwawrzyniak.getby


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentLoginBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

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

        val username = binding.loginInputField.toString()
        val password = binding.passwordInputField.toString()

        binding.loginSignInButton.setOnClickListener {  viewModel.login(
            username,
            password
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { Timber.i("lol") }
        }

        viewModel.loginResultLiveData.observe(viewLifecycleOwner){ loginResult ->
            when(loginResult){
                is LoginResult.Success -> navigateToDashboard()
                is LoginResult.Fail -> handleFailedLoginAttempt(loginResult)
            }
        }

        return binding.root
    }

    private fun handleFailedLoginAttempt(loginResult: LoginResult.Fail) {
        binding.loginInputLayout.error = loginResult.error
        binding.passwordInputField.setText("")
        binding.passwordInputLayout.isErrorEnabled = true
    }

    private fun navigateToDashboard(){
        nav_host.findNavController().navigate(R.id.placeholder)
    }
}
