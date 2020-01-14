package com.rwawrzyniak.getby.dagger

import android.app.Activity
import androidx.fragment.app.Fragment

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9

interface DaggerComponentProvider {
    val component: ApplicationComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component

val Fragment.injector get() = (activity?.application as DaggerComponentProvider).component
