package com.rwawrzyniak.getby.authorization.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.authorization.LoginResult
import com.rwawrzyniak.getby.authorization.LoginUseCase
import io.reactivex.Completable

class LoginViewModel internal constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    val loginResultLiveData: MutableLiveData<LoginResult> = MutableLiveData(
        LoginResult.Default
    )

    fun login(username: String, password: String): Completable {
        return loginUseCase.login(username, password).flatMapCompletable { loginResult ->
            Completable.fromAction { loginResultLiveData.postValue(loginResult) }
        }
    }
}
