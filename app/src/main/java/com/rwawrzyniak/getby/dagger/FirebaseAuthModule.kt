package com.rwawrzyniak.getby.dagger

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
object FirebaseAuthModule {
    @JvmStatic @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}