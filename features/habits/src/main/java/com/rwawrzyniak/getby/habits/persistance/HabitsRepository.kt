package com.rwawrzyniak.getby.habits.persistance

import com.rwawrzyniak.getby.core.AppDatabase
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class HabitsRepository @Inject internal constructor(private val database: AppDatabase) {
    fun loadHabits(): Single<List<Habit>> = database.habitDao().getAll()
    fun getSingle(id: String): Single<Habit> = database.habitDao().getById(id)
    fun saveHabit(habit: Habit): Completable = database.habitDao().insert(habit)
    fun removeHabit(habit: Habit): Completable = database.habitDao().delete(habit)
    fun updateHabit(habit: Habit): Completable = database.habitDao().update(habit)
}
