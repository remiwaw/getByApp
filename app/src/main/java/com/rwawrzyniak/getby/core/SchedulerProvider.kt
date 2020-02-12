package com.rwawrzyniak.getby.core

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * This class collects schedulers for easy consumption and mockability
 */
interface SchedulerProvider {
    /**
     * Returns a scheduler for work that should be executed on Android's main thread
     */
    fun main(): Scheduler

    /**
     * Returns a scheduler for heavy-weight, io-bound work
     */
    fun io(): Scheduler

    /**
     * Returns a scheduler for light-weight, non-io work
     */
    fun computation(): Scheduler

    /**
     * Returns a scheduler that creates a new thread for each workload
     */
    fun newThread(): Scheduler
}

object DefaultSchedulerProvider : SchedulerProvider {
    /**
     * Returns a scheduler for work that should be executed on Android's main thread
     */
    override fun main(): Scheduler = AndroidSchedulers.mainThread()

    /**
     * Returns a scheduler for heavy-weight, io-bound work
     */
    override fun io(): Scheduler = Schedulers.io()

    /**
     * Returns a scheduler for light-weight, non-io work
     */
    override fun computation(): Scheduler = Schedulers.computation()

    /**
     * Returns a scheduler that creates a new thread for each workload
     */
    override fun newThread(): Scheduler = Schedulers.newThread()
}
