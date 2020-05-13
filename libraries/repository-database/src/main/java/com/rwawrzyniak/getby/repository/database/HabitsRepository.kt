package com.rwawrzyniak.getby.repository.database

import com.rwawrzyniak.getby.abstractconverter.AbstractConverter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import com.rwawrzyniak.getby.abstractrepository.AbstractRepository
import com.rwawrzyniak.getby.converters.HabitConverter
import com.rwawrzyniak.getby.database.HabitDao
import com.rwawrzyniak.getby.datasource.abstract.AbstractDataSource

// TODO Fix casting problems here
class HabitsRepository(habitDao: HabitDao, habitConverter: HabitConverter) :
	AbstractRepository<AbstractDataSource<AbstractEntity>, AbstractConverter<AbstractModel, AbstractEntity>>(
		habitDao as AbstractDataSource<AbstractEntity>,
		habitConverter as AbstractConverter<AbstractModel, AbstractEntity>
	)
