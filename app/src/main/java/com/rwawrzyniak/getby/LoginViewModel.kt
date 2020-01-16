package com.rwawrzyniak.getby

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject internal constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    val loginResult: MutableLiveData<LoginResult> = MutableLiveData()

    fun login(username: String, password: String) = loginUseCase.login(username, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                loginResult.postValue(LoginResult.Success)
            } else {
                loginResult.postValue(LoginResult.Fail(it.exception.toString()))
            }
        }
}