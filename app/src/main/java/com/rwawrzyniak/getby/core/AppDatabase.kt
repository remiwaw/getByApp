package com.rwawrzyniak.getby.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rwawrzyniak.getby.habits.persistance.Habit
import com.rwawrzyniak.getby.habits.persistance.HabitDao
import com.rwawrzyniak.getby.habits.persistance.RoomConverters

@Database(entities = [Habit::class], version = 2)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun habitDao(): HabitDao

}
