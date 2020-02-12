package com.rwawrzyniak.getby.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import javax.inject.Inject


class RegisterViewModel @Inject internal constructor(
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