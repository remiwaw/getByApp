package com.rwawrzyniak.getby.dagger

import android.content.Context
import android.content.SharedPreferences
import com.rwawrzyniak.getby.core.android.preferences.PreferencesHelper
import com.rwawrzyniak.getby.core.android.di.CoreComponent
import com.rwawrzyniak.getby.login.LoginViewModel
import com.rwawrzyniak.getby.register.RegisterViewModel
import dagger.BindsInstance
import dagger.Component

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9
@Component(modules = [
	ApplicationModule::class
], dependencies = [CoreComponent::class])
@AppScope
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationContext: Context
        ): ApplicationComponent
    }

    val loginViewModel: LoginViewModel
    val registerViewModel: RegisterViewModel
	fun prefManager(): SharedPreferences
	fun preferencesHelper(): PreferencesHelper
}
