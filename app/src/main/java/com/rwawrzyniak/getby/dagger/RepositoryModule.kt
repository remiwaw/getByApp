package com.rwawrzyniak.getby.dagger

import com.rwawrzyniak.getby.core.AppDatabase
import com.rwawrzyniak.getby.habits.persistance.HabitsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

	@Singleton
    @Provides
    fun providesHabitsRepository(database: AppDatabase): HabitsRepository =
		HabitsRepository(database)
}
