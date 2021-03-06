package com.rwawrzyniak.getby.database

import androidx.annotation.VisibleForTesting
import com.rwawrzyniak.getby.entities.Habit
import io.reactivex.Completable
import io.reactivex.Single

class HabitDatabaseSource @VisibleForTesting constructor(private val habitDao: HabitDao) : HabitDao {
	override fun getById(id: String): Single<Habit> = habitDao.getById(id)
	override fun getAll(): Single<List<Habit>> = habitDao.getAll()
	override fun insert(entity: Habit): Completable = habitDao.insert(entity)
	override fun delete(entity: Habit): Completable = habitDao.delete(entity)
	override fun update(entity: Habit): Completable = habitDao.update(entity)
}
