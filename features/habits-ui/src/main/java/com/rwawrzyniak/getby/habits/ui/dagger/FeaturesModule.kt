package com.rwawrzyniak.getby.habits.ui.dagger

import dagger.Module
import dagger.Provides

@Module
class FeaturesModule {
    @Provides
    fun provideInt() = 1
}
