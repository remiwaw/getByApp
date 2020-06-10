package com.rwawrzyniak.getby.authorization

import io.reactivex.Single

class LoginUseCase internal constructor(
	private val authRepository: AuthEmailRepository,
	private val firebaseExceptionMapper: FirebaseAuthExceptionMapper
) {
    fun login(userName: String, password: String): Single<LoginResult> {
        return authRepository.login(userName, password).map {
            authResult -> LoginResult.Success(authResult.additionalUserInfo.username) as LoginResult
        }.onErrorReturn {
            LoginResult.Fail(firebaseExceptionMapper.mapToString(it))
        }
    }
}
