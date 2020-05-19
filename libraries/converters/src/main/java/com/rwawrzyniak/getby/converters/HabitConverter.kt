package com.rwawrzyniak.getby.converters

import com.rwawrzyniak.getby.abstractconverter.AbstractConverter
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.abstractmodel.AbstractModel
import com.rwawrzyniak.getby.entities.Habit
import com.rwawrzyniak.getby.entities.HabitDay
import com.rwawrzyniak.getby.models.Frequency
import com.rwawrzyniak.getby.models.HabitModel
import com.rwawrzyniak.getby.models.HourMinute
import com.rwawrzyniak.getby.models.Reminder

class HabitConverter : AbstractConverter<HabitModel, Habit> {
	override fun toModel(entity: Habit): HabitModel {
		return HabitModel(
			entity.id,
			entity.name,
			entity.description,
			frequency = entity.frequencyToModel(),
			reminder = entity.reminderToModel(),
			history = entity.historyToModel(),
			isArchived = entity.isArchived
		)
	}

	override fun toEntity(model: HabitModel): Habit {
		return Habit(
			model.id,
			model.name,
			model.description,
			frequency = model.frequencyToEntity(),
			reminder = model.reminderToEntity(),
			history = model.historyToEntity(),
			isArchived = model.isArchived
		)
	}

	// Helper method for converting model to entity
	private fun HabitModel.frequencyToEntity() =  com.rwawrzyniak.getby.entities.Frequency(
		this.frequency.times,
		this.frequency.cycle
	)
	private fun HabitModel.reminderToEntity(): com.rwawrzyniak.getby.entities.Reminder? {
		val modelReminder = this.reminder
		return if(modelReminder != null){
			return com.rwawrzyniak.getby.entities.Reminder(
				com.rwawrzyniak.getby.entities.HourMinute(
					modelReminder.time.hour,
					modelReminder.time.minutes
				),
				requireNotNull(
					modelReminder.days)
			)
		} else {
			null
		}
	}
	private fun HabitModel.historyToEntity(): List<HabitDay> {
		return history.map { HabitDay(it.day, it.checked) }.toList()
	}

	// Helper method for converting entiy to moddel
	private fun Habit.frequencyToModel() =  Frequency(this.frequency.times, this.frequency.cycle)
	private fun Habit.reminderToModel(): Reminder? {
		val entityReminder = this.reminder
		return if(entityReminder != null){
			return Reminder(HourMinute(entityReminder.time.hour, entityReminder.time.minutes), requireNotNull(
				entityReminder.days))
		} else {
			null
		}
	}
	private fun Habit.historyToModel(): List<com.rwawrzyniak.getby.models.HabitDay> {
		return history.map { com.rwawrzyniak.getby.models.HabitDay(it.day, it.checked) }.toList()
	}
}
