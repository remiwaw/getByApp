package com.rwawrzyniak.getby.authorization

import io.reactivex.Single

class RegisterUseCase internal constructor(
	private val authRepository: AuthEmailRepository,
	private val firebaseExceptionMapper: FirebaseAuthExceptionMapper
) {
    fun register(userName: String, password: String): Single<RegisterResult> =
        authRepository.register(userName, password).map {
                 RegisterResult.Success as RegisterResult
        }.onErrorReturn {
            RegisterResult.Fail(firebaseExceptionMapper.mapToString(it))
        }
}
