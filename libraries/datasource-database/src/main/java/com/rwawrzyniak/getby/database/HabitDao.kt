package com.rwawrzyniak.getby.database

import androidx.room.*
import com.rwawrzyniak.getby.datasource.AbstractDataSource
import com.rwawrzyniak.getby.entities.Habit
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface HabitDao : AbstractDataSource<Habit>{

	@Query("SELECT * FROM habit WHERE id=:id ")
	override fun getById(id: String): Single<Habit>

	@Query("SELECT * FROM habit")
	override fun getAll(): Single<List<Habit>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	override fun insert(entity: Habit): Completable

	@Delete
	override fun delete(entity: Habit) : Completable

	@Update
	override fun update(entity: Habit): Completable
}
