package com.rwawrzyniak.getby.authorization.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.authorization.RegisterResult
import com.rwawrzyniak.getby.authorization.RegisterUseCase
import io.reactivex.Completable

class RegisterViewModel internal constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    val registerResultLiveData: MutableLiveData<RegisterResult> = MutableLiveData(
        RegisterResult.Default
    )

    fun login(username: String, password: String): Completable {
        return registerUseCase.register(username, password).flatMapCompletable { registerResult ->
            Completable.fromAction { registerResultLiveData.postValue(registerResult) }
        }
    }
}
