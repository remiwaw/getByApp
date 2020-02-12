package com.rwawrzyniak.getby.core

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

object TestSchedulerProvider {
    @JvmStatic
    @JvmOverloads
    fun create(
        io: Scheduler = Schedulers.trampoline(),
        main: Scheduler = Schedulers.trampoline(),
        computation: Scheduler = Schedulers.trampoline(),
        newThread: Scheduler = Schedulers.trampoline()
    ): SchedulerProvider = object : SchedulerProvider {
        override fun main(): Scheduler = main

        override fun io(): Scheduler = io

        override fun computation(): Scheduler = computation

        override fun newThread(): Scheduler = newThread
    }
}
