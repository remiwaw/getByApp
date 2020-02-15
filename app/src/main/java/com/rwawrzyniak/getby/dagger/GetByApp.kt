package com.rwawrzyniak.getby.dagger

import android.app.Application
import com.rwawrzyniak.getby.BuildConfig
import timber.log.Timber

class GetByApp : Application(), DaggerComponentProvider {

    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory()
            .create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
