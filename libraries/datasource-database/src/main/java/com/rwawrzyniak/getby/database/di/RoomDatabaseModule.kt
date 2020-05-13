package com.rwawrzyniak.getby.database.di

import android.content.Context
import androidx.room.Room
import com.rwawrzyniak.getby.database.AppDatabase
import com.rwawrzyniak.getby.database.HabitDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomDatabaseModule {

	private lateinit var appDatabase: AppDatabase

	fun RoomDatabaseModule(context: Context) {
		appDatabase =
			Room.databaseBuilder(
				context,
				AppDatabase::class.java, "GetBy.db"
			).fallbackToDestructiveMigration()
				.build()
	}

	@Singleton
	@Provides
	fun provideDatabase(context: Context) = appDatabase


	@Singleton
	@Provides
	fun providesHabitDao(appDatabase: AppDatabase): HabitDao {
		return appDatabase.habitDao()
	}
}
