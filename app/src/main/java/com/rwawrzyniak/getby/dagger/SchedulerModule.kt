package com.rwawrzyniak.getby.dagger

import com.rwawrzyniak.getby.core.DefaultSchedulerProvider
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.core.TestSchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SchedulerModule {

    @Provides
    @Named("schedulerProvider")
    fun provideSchedulerProvider(): SchedulerProvider = DefaultSchedulerProvider

    @Provides
    @Named("testSchedulerProvider")
    fun provideTestSchedulerProvider(): SchedulerProvider = TestSchedulerProvider.create()
}
