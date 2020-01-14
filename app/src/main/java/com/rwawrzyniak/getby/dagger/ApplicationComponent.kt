package com.rwawrzyniak.getby.dagger

import android.content.Context
import dagger.BindsInstance
import dagger.Component

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9
@Component
interface ApplicationComponent {

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance applicationContext: Context
        ) : ApplicationComponent
    }
}