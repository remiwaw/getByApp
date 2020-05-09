package com.rwawrzyniak.getby.repository.database

import com.rwawrzyniak.getby.abstractrepository.AbstractRepository
import com.rwawrzyniak.getby.converters.HabitConverter
import com.rwawrzyniak.getby.database.HabitDao
import com.rwawrzyniak.getby.models.HabitModel

class HabitsRepository(habitDao: HabitDao, habitConverter: HabitConverter) :
	AbstractRepository<HabitModel, HabitDao, HabitConverter>(
		habitDao,
		habitConverter
	)
