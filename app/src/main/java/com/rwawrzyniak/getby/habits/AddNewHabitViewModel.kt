package com.rwawrzyniak.getby.habits

import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.SchedulerModule
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import javax.inject.Named

// TODO add error handling
class AddNewHabitViewModel @Inject internal constructor(
    @Named(SchedulerModule.SCHEDULER_PROVIDER) private val schedulerProvider: SchedulerProvider,
    private val habitsRepository: HabitsRepository
) : ViewModel() {
    val habitBuilder = Habit.Builder()
	private val compositeDisposable = CompositeDisposable()

    fun saveHabit(){
        habitsRepository.saveHabit(habitBuilder.build())
			.subscribeOn(schedulerProvider.io())
			.observeOn(schedulerProvider.main())
			.subscribe()
			.addTo(compositeDisposable)
	}

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}
}
