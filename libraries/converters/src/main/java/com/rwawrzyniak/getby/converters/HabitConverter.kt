package com.rwawrzyniak.getby.converters

import com.rwawrzyniak.getby.abstractconverter.AbstractConverter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import com.rwawrzyniak.getby.entities.Habit
import com.rwawrzyniak.getby.models.HabitModel

class HabitConverter : AbstractConverter<HabitModel, Habit> {
	override fun toModel(entity: Habit): HabitModel {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun toEntity(model: HabitModel): Habit {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}
