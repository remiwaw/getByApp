package com.rwawrzyniak.getby.database.di

object RoomDatabaseInjectorHelper {
    fun provideComponent(implementingClass: Any): RoomDatabaseComponent {
        return if (implementingClass is RoomDatabaseComponentProvider) {
            implementingClass.provideComponent()
        } else {
            throw IllegalStateException("The application context you have passed does not implement ComponentProvider")
        }
    }
}
