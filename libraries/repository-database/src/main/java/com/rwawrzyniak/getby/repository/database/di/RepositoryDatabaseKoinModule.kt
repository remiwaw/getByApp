package com.rwawrzyniak.getby.repository.database.di

import com.rwawrzyniak.getby.converters.di.convertersModule
import com.rwawrzyniak.getby.core.android.di.dataSourceDatabaseModule
import com.rwawrzyniak.getby.repository.database.HabitsRepository
import org.koin.dsl.module

val repositoryDatabaseModule = dataSourceDatabaseModule + convertersModule +
	module {
		single {
			HabitsRepository(get(), get())
		}
	}
