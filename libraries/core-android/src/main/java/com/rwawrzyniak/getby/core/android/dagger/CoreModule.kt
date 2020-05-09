package com.rwawrzyniak.getby.core.android.dagger

import com.rwawrzyniak.getby.core.android.ExpensiveObject
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {
    @Provides
    @Singleton
    fun provideExpensiveObject(): ExpensiveObject = ExpensiveObject()

}
