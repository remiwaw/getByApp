package com.rwawrzyniak.getby.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rwawrzyniak.getby.abstractentities.AbstractEntity
import com.rwawrzyniak.getby.datasource.abstract.AbstractDataSource
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
	override fun insert(entity: AbstractEntity): Completable

	@Delete
	override fun delete(habit: AbstractEntity) : Completable

	@Update
	override fun update(habit: AbstractEntity): Completable
}
