package com.rwawrzyniak.getby.core.android.dagger

import com.rwawrzyniak.getby.core.AppDatabase
import com.rwawrzyniak.getby.database.AppDatabase
import com.rwawrzyniak.getby.habits.persistance.HabitsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

	@Singleton
    @Provides
    fun providesHabitsRepository(database: com.rwawrzyniak.getby.database.AppDatabase): HabitsRepository =
		HabitsRepository(database)
}
