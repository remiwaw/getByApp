package com.rwawrzyniak.getby

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginViewModel @Inject internal constructor(private val auth: FirebaseAuth): ViewModel() {
    fun injectionTest() = auth.app
}