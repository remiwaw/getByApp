package com.rwawrzyniak.getby.database.di

import com.rwawrzyniak.getby.database.HabitDatabaseSource
import com.rwawrzyniak.getby.database.di.RoomDatabaseModule
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Component(modules = [RoomDatabaseModule::class])
@Singleton
interface RoomDatabaseComponent {
    fun getHabitDatabaseSource(): HabitDatabaseSource
}
