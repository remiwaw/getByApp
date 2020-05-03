package com.rwawrzyniak.getby.dagger

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.rwawrzyniak.getby.core.DateTimeProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ApplicationModule {
	@JvmStatic @Provides
	fun provideResources(context: Context): Resources = context.resources

	@JvmStatic @Provides
	fun provideDateTimeProvider() =
		DateTimeProvider()

	@Provides
	@Singleton
	fun provideSharedPreference(context: Context): SharedPreferences {
		return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
	}

	private const val PREF_FILE_NAME = "com.rwawrzyniak.getby.prefs"
}
