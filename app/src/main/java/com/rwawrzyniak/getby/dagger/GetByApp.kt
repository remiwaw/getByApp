package com.rwawrzyniak.getby.dagger

import android.app.Application
import com.rwawrzyniak.getby.habits.ui.di.habitsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GetByApp : Application() {

    override fun onCreate() {
        super.onCreate()
		startKoin {
			androidLogger()
			androidContext(this@GetByApp)
			// declare modules
			modules(listOf(habitsModule))
		}
	}


//    private fun setupTimber() {
//        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
//    }
}
