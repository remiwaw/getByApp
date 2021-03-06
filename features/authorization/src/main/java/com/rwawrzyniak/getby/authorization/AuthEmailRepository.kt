package com.rwawrzyniak.getby.authorization

import com.androidhuman.rxfirebase2.auth.rxCreateUserWithEmailAndPassword
import com.androidhuman.rxfirebase2.auth.rxSignInWithEmailAndPassword
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

class AuthEmailRepository internal constructor(private val auth: FirebaseAuth) {

    fun register(
        email: String,
        password: String
    ): Single<FirebaseUser> = auth.rxCreateUserWithEmailAndPassword(email, password)

    fun login(
        email: String,
        password: String
    ): Single<AuthResult> = auth.rxSignInWithEmailAndPassword(email, password)
}
