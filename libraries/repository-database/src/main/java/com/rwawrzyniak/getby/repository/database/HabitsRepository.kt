package com.rwawrzyniak.getby.repository.database

import com.rwawrzyniak.getby.abstractrepository.AbstractRepository
import com.rwawrzyniak.getby.converters.HabitConverter
import com.rwawrzyniak.getby.database.HabitDatabaseSource

// TODO Fix casting problems here
class HabitsRepository(habitDatabaseSource: HabitDatabaseSource, habitConverter: HabitConverter) :
	AbstractRepository<HabitDatabaseSource, HabitConverter>(
		habitDatabaseSource,
		habitConverter
	)
