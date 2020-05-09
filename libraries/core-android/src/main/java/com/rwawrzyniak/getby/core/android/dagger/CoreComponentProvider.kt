package com.rwawrzyniak.getby.core.android.dagger

interface CoreComponentProvider {
	val component: CoreComponent
}

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9

// interface CoreComponentProvider {
// 	val component: ApplicationComponent
// }
//
// val Activity.injector get() = (application as CoreComponentProvider).component
//
// val Fragment.injector get() = (activity?.application as CoreComponentProvider).component

