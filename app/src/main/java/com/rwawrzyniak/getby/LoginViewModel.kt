package com.rwawrzyniak.getby

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import javax.inject.Inject


class LoginViewModel @Inject internal constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    val loginResultLiveData: MutableLiveData<LoginResult> = MutableLiveData(LoginResult.Default)

    fun login(username: String, password: String): Completable {
        return loginUseCase.login(username, password).flatMapCompletable { loginResult ->
            Completable.fromAction { loginResultLiveData.postValue(loginResult) }
        }
    }
}