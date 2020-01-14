package com.rwawrzyniak.getby.dagger

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// https://proandroiddev.com/dagger-2-on-android-the-simple-way-f706a2c597e9

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.viewModel(crossinline factory: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
        }
    }

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.viewModel(crossinline factory: () -> T) =
    viewModels<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
        }
    }

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.activityViewModel(crossinline factory: () -> T) =
    activityViewModel<T> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
        }
    }