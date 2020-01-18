package com.rwawrzyniak.getby

import io.reactivex.Single
import javax.inject.Inject

class LoginUseCase @Inject internal constructor(
    private val authRepository: AuthEmailRepository,
    private val firebaseExceptionMapper: FirebaseExceptionMapper
) {
    fun login(userName: String, password: String): Single<LoginResult> {
        return authRepository.login(userName, password).map {
            authResult -> LoginResult.Success(authResult.additionalUserInfo.username) as LoginResult
        }.onErrorReturn {
            LoginResult.Fail(firebaseExceptionMapper.mapToString(it))
        }
    }
}
