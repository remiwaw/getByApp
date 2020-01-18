package com.rwawrzyniak.getby

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import javax.inject.Inject

class RegisterUseCase @Inject internal constructor(
    private val authRepository: AuthEmailRepository
){
    fun register(userName: String, password: String): Single<FirebaseUser> =
        authRepository.register(userName, password)
}