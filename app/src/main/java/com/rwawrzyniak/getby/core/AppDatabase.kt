package com.rwawrzyniak.getby.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rwawrzyniak.getby.habits.Habit
import com.rwawrzyniak.getby.habits.HabitDao
import com.rwawrzyniak.getby.habits.RoomConverters

@Database(entities = [Habit::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun habitDao(): HabitDao
}
