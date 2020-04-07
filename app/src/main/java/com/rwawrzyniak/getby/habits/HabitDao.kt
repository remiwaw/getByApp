package com.rwawrzyniak.getby.habits

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface HabitDao {

	@Query("SELECT * FROM habit WHERE id=:id ")
	fun getSingle(id: String): Single<Habit>

	@Query("SELECT * FROM habit")
	fun getAll(): LiveData<MutableList<Habit>>

	@Insert
	fun insert(vararg habit: Habit) : Completable

	@Delete
	fun delete(habit: Habit) : Completable

	@Update
	fun update(habit: Habit): Completable
}
