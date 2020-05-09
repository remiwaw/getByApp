package com.rwawrzyniak.getby.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rwawrzyniak.getby.entities.Habit

@Database(entities = [Habit::class], version = 2)
@TypeConverters(RoomMarshal::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun habitDao(): HabitDao
}
