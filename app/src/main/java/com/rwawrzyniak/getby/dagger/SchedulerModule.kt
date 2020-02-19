package com.rwawrzyniak.getby.dagger

import com.rwawrzyniak.getby.core.DefaultSchedulerProvider
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.core.TestSchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object SchedulerModule {

    const val SCHEDULER_PROVIDER = "schedulerProvider"
    const val TEST_SCHEDULER_PROVIDER = "testSchedulerProvider"

    @Provides
    @Named(SCHEDULER_PROVIDER)
    fun provideSchedulerProvider(): SchedulerProvider = DefaultSchedulerProvider

    @Provides
    @Named(TEST_SCHEDULER_PROVIDER)
    fun provideTestSchedulerProvider(): SchedulerProvider = TestSchedulerProvider.create()
}
