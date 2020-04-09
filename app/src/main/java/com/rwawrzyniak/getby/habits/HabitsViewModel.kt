package com.rwawrzyniak.getby.habits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.BusModule.GLOBAL_EVENT_SUBJECT
import com.rwawrzyniak.getby.dagger.SchedulerModule.SCHEDULER_PROVIDER
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

class HabitsViewModel @Inject internal constructor(
    @Named(GLOBAL_EVENT_SUBJECT) private val globalEventSubject: PublishSubject<GlobalEvent>,
    @Named(SCHEDULER_PROVIDER) private val schedulerProvider: SchedulerProvider,
    private val habitsRepository: HabitsRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    // Private, mutable backing field - only update values internally
    private val _isBusy: MutableLiveData<Boolean> = MutableLiveData()
    private val _firstDay: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())

    // Observers will subscribe to this since it is immutable to them
    val isBusy: MutableLiveData<Boolean>
        get() = _isBusy
    val habits: LiveData<MutableList<Habit>> = habitsRepository.loadHabits()
    val firstDay: MutableLiveData<LocalDate>
        get() = _firstDay

    init {
        globalEventSubject
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe { firstDay.postValue((LocalDate.now())) }
            .addTo(compositeDisposable)
    }

	fun removeHabit(habit: Habit){
		simplySubscribe(habitsRepository.removeHabit(habit))
	}

	fun archiveHabit(habit: Habit){
		habit.isArchived = true
		updateHabit(habit)
	}

	fun updateHabit(habit: Habit){
		simplySubscribe(habitsRepository.updateHabit(habit))
	}

	private fun simplySubscribe(completable: Completable){
		completable.subscribeOn(schedulerProvider.io())
			.observeOn(schedulerProvider.main())
			.subscribe(	)
			.addTo(compositeDisposable)
	}

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
