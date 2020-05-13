package com.rwawrzyniak.getby.repository.database.di

import com.rwawrzyniak.getby.repository.database.HabitsRepository
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Component(modules = [RepositoryModule::class])
@Singleton
interface RepositoryComponent {
    @Named("HabitsRepository") fun getHabitsRepository(): HabitsRepository
}
