package com.rwawrzyniak.getby.habits

import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.SchedulerModule
import javax.inject.Inject
import javax.inject.Named

class AddNewHabitViewModel @Inject internal constructor(
    @Named(SchedulerModule.SCHEDULER_PROVIDER) private val schedulerProvider: SchedulerProvider,
    private val habitsRepository: HabitsRepository
) : ViewModel() {
    val habitBuilder : Habit.Builder = Habit.Builder()

    fun saveHabit(){

    }
}