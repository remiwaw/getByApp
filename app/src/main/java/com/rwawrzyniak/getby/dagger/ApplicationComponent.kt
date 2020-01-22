package com.rwawrzyniak.getby.dagger

import android.content.Context
import com.rwawrzyniak.getby.LoginViewModel
import com.rwawrzyniak.getby.rxjava.SchedulerProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9
@Component(modules = [FirebaseAuthModule::class, SchedulerModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance applicationContext: Context
        ) : ApplicationComponent
    }

    val loginViewModel : LoginViewModel
    @Named("schedulerProvider") fun provideSchedulerProvider(): SchedulerProvider
}

