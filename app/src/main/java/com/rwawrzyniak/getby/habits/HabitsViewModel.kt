package com.rwawrzyniak.getby.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.BusModule.GLOBAL_EVENT_SUBJECT
import com.rwawrzyniak.getby.dagger.SchedulerModule.SCHEDULER_PROVIDER
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.util.Calendar
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
    private val _habits: MutableLiveData<List<Habit>> = MutableLiveData()
    private val _firstDay: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())

    // Observers will subscribe to this since it is immutable to them
    val isBusy: MutableLiveData<Boolean>
        get() = _isBusy
    val habits: MutableLiveData<List<Habit>>
        get() = _habits
    val firstDay: MutableLiveData<Calendar>
        get() = _firstDay

    init {
        habitsRepository.loadHabits()
            .doOnSubscribe { isBusy.postValue(true) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .doFinally { isBusy.postValue(false) }
            .subscribe { habits.postValue(it) }
            .addTo(compositeDisposable)

        globalEventSubject
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe { firstDay.postValue((Calendar.getInstance())) }
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
