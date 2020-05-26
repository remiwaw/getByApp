package com.rwawrzyniak.getby.repository.database

import com.rwawrzyniak.getby.abstractrepository.AbstractRepository
import com.rwawrzyniak.getby.converters.HabitConverter
import com.rwawrzyniak.getby.database.HabitDatabaseSource
import com.rwawrzyniak.getby.entities.Habit
import com.rwawrzyniak.getby.models.HabitModel

class HabitsRepository(habitDatabaseSource: HabitDatabaseSource, habitConverter: HabitConverter) :
	AbstractRepository<Habit, HabitModel>(
		habitDatabaseSource,
		habitConverter
	)
