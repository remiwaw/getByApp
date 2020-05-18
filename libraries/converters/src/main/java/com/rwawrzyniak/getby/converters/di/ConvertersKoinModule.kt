package com.rwawrzyniak.getby.converters.di
import com.rwawrzyniak.getby.converters.HabitConverter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val convertersModule =
    module {
        single(named("DatabaseHabitConverter")) { HabitConverter() }
    }
