package com.rwawrzyniak.getby.converters.di

import com.rwawrzyniak.getby.converters.HabitConverter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConvertersModule {
    @Provides
    @Singleton
    fun provideHabitRoomConverter() = HabitConverter()
}
