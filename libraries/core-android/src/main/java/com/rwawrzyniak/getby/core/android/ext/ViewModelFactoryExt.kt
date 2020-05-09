package com.rwawrzyniak.getby.core.android.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9
// https://github.com/tfcporciuncula/dagger-journey/

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.fragmentScopedViewModel(crossinline factory: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
        }
    }

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.activityScopedViewModel(crossinline factory: () -> T) =
    activityViewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
        }
    }
