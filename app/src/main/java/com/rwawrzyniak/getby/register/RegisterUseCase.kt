package com.rwawrzyniak.getby.register

import com.rwawrzyniak.getby.AuthEmailRepository
import com.rwawrzyniak.getby.core.FirebaseExceptionMapper
import io.reactivex.Single
import javax.inject.Inject

class RegisterUseCase @Inject internal constructor(
    private val authRepository: AuthEmailRepository,
    private val firebaseExceptionMapper: FirebaseExceptionMapper
) {
    fun register(userName: String, password: String): Single<RegisterResult> =
        authRepository.register(userName, password).map {
                 RegisterResult.Success as RegisterResult
        }.onErrorReturn {
            RegisterResult.Fail(firebaseExceptionMapper.mapToString(it))
        }
}
