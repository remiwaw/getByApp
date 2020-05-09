package com.rwawrzyniak.getby.dagger

import android.app.Application
import com.rwawrzyniak.getby.BuildConfig
import com.rwawrzyniak.getby.core.android.dagger.CoreComponent
import com.rwawrzyniak.getby.core.android.dagger.CoreComponentProvider
import timber.log.Timber

class GetByApp : Application(), CoreComponentProvider {
    override val component: CoreComponent by lazy {
		DaggerCoreComponent
			.builder()
			.build()
    }

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
