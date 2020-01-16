package com.rwawrzyniak.getby

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class RegisterUseCase @Inject internal constructor(
    private val authRepository: AuthEmailRepository
){
    fun register(userName: String, password: String): Task<AuthResult> =
        authRepository.register(userName, password)
}