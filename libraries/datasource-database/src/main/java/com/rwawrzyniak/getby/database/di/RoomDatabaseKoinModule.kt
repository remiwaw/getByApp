package com.rwawrzyniak.getby.database.di

import androidx.room.Room
import com.rwawrzyniak.getby.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataSourceDatabaseModule =
	module {
		single {
			Room.databaseBuilder(
				androidApplication(),
				AppDatabase::class.java, "GetBy.db"
			).fallbackToDestructiveMigration()
				.build()
		}

		// Expose UserDao directly
		single { get<AppDatabase>().habitDao() }

	}
