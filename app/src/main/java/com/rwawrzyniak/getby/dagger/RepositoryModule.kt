package com.rwawrzyniak.getby.dagger

import com.rwawrzyniak.getby.habits.HabitsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

	@Singleton
    @Provides
    fun providesHabitsRepository(): HabitsRepository = HabitsRepository()
}
