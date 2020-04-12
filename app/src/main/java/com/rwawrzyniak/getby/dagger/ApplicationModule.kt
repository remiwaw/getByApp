package com.rwawrzyniak.getby.dagger

import android.content.Context
import android.content.res.Resources
import com.rwawrzyniak.getby.core.DateTimeProvider
import dagger.Module
import dagger.Provides

@Module
object ApplicationModule {
	@JvmStatic @Provides
	fun provideResources(context: Context): Resources = context.resources

	@JvmStatic @Provides
	fun provideDateTimeProvider() = DateTimeProvider()
}