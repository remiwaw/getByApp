package com.rwawrzyniak.getby.repository.database

import com.rwawrzyniak.getby.abstractrepository.AbstractRepository
import com.rwawrzyniak.getby.converters.HabitConverter
import com.rwawrzyniak.getby.database.HabitDatabaseSource
import com.rwawrzyniak.getby.entities.Habit
import com.rwawrzyniak.getby.models.HabitModel
import io.reactivex.Completable
import io.reactivex.Single

class HabitsRepository(habitDatabaseSource: HabitDatabaseSource, habitConverter: HabitConverter) :
	AbstractRepository<Habit, HabitModel>(
		habitDatabaseSource,
		habitConverter
	){

	override fun getById(id: String): Single<HabitModel> = super.getById(id)

	override fun getAll(): Single<MutableList<HabitModel>> = super.getAll()

	override fun insert(model: HabitModel): Completable = super.insert(model)

	override fun delete(model: HabitModel): Completable = super.delete(model)

	override fun update(model: HabitModel): Completable = super.update(model)
}
