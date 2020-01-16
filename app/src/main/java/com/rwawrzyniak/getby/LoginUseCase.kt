package com.rwawrzyniak.getby

import javax.inject.Inject

class LoginUseCase @Inject internal constructor(
    private val authRepository: AuthEmailRepository
) {
    fun login(userName: String, password: String) = authRepository.login(userName, password)
}