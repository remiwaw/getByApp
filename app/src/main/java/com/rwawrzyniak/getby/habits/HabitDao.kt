package com.rwawrzyniak.getby.habits

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable

@Dao
interface HabitDao {
	@Query("SELECT * FROM habit")
	fun getAll(): LiveData<List<Habit>>

	@Insert
	fun insert(vararg habit: Habit) : Completable

	@Delete
	fun delete(habit: Habit) : Completable

	@Update
	fun update(habit: Habit): Completable
}
