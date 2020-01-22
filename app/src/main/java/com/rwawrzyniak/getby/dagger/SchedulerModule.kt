package com.rwawrzyniak.getby.dagger

import com.rwawrzyniak.getby.rxjava.DefaultSchedulerProvider
import com.rwawrzyniak.getby.rxjava.SchedulerProvider
import com.rwawrzyniak.getby.rxjava.TestSchedulerProvider
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