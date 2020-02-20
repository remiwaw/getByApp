package com.rwawrzyniak.getby.habits

import io.reactivex.Observable
import javax.inject.Inject

class HabitsRepository @Inject internal constructor() {

   fun loadHabits(): Observable<List<Habit>> {
       return Observable.just(listOf(
           Habit("habitTitle1", "blaablablabla hgabit one"),
           Habit("habitTitle2", "blaablablabla hgabit two")
       ))
   }
}
