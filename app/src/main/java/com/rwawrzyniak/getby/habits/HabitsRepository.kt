package com.rwawrzyniak.getby.habits

import com.rwawrzyniak.getby.core.ObservableList
import io.reactivex.Observable
import javax.inject.Inject

class HabitsRepository @Inject internal constructor() {

    private val habits = ObservableList(
        mutableListOf(
            Habit("habitTitle1", "blaablablabla hgabit one"),
            Habit("habitTitle2", "blaablablabla hgabit two")
        )
    )

    fun loadHabits(): Observable<List<Habit>> = habits.observe()

    fun saveHabit(habit: Habit){
        habits.add(habit)
    }
}
