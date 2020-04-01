package com.rwawrzyniak.getby.habits

import androidx.lifecycle.LiveData
import com.rwawrzyniak.getby.core.AppDatabase
import io.reactivex.Completable
import javax.inject.Inject

class HabitsRepository @Inject internal constructor(private val database: AppDatabase) {

    fun loadHabits(): LiveData<List<Habit>> = database.habitDao().getAll()
    fun saveHabit(habit: Habit): Completable = database.habitDao().insert(habit)
    fun removeHabit(habit: Habit): Completable = database.habitDao().delete(habit)
}
