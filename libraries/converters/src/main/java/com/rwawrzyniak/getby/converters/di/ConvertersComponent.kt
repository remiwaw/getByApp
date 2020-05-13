package com.rwawrzyniak.getby.converters.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = [ConvertersModule::class])
@Singleton
interface ConvertersComponent {
    fun getComponent(): ConvertersComponent
}
