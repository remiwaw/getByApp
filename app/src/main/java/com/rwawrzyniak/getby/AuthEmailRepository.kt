package com.rwawrzyniak.getby

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthEmailRepository @Inject internal constructor(private val auth: FirebaseAuth) {

    fun register(
        email: String,
        password: String
    ): Task<AuthResult> = auth.createUserWithEmailAndPassword(email, password)

    fun login(
        email: String,
        password: String
    ): Task<AuthResult> = auth.signInWithEmailAndPassword(email, password)
}