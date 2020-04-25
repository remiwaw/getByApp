package com.rwawrzyniak.getby.habits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.BusModule.GLOBAL_EVENT_SUBJECT
import com.rwawrzyniak.getby.dagger.SchedulerModule.SCHEDULER_PROVIDER
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

class HabitsViewModel @Inject internal constructor(
    @Named(GLOBAL_EVENT_SUBJECT) private val globalEventSubject: PublishSubject<GlobalEvent>,
    @Named(SCHEDULER_PROVIDER) private val schedulerProvider: SchedulerProvider,
    private val habitsRepository: HabitsRepository,
	private val dateTimeProvider: DateTimeProvider
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    // Private, mutable backing field - only update values internally
    private val _isBusy: MutableLiveData<Boolean> = MutableLiveData()
    private val _firstDay: MutableLiveData<LocalDate> = MutableLiveData(dateTimeProvider.getCurrentDate())

    // Observers will subscribe to this since it is immutable to them
    val isBusy: MutableLiveData<Boolean>
        get() = _isBusy
    val originalHabits: Single<List<Habit>> = habitsRepository.loadHabits()
	val filteredHabits: MutableList<Habit> = arrayListOf()
	val oldFiteredHabits: MutableList<Habit> = arrayListOf()

	private var isShowArchivedFilterOn = false
	val firstDay: MutableLiveData<LocalDate>
        get() = _firstDay

    init {
		globalEventSubject
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe { firstDay.postValue((LocalDate.now())) }
            .addTo(compositeDisposable)
    }

	fun removeHabit(habit: Habit): Completable = habitsRepository.removeHabit(habit)

	fun archiveHabit(habit: Habit): Completable {
		habit.isArchived = true
		return updateHabit(habit)
	}

	fun updateHabit(habit: Habit): Completable = habitsRepository.updateHabit(habit)

	fun filter(query: String, showArchived: Boolean?): Completable {
		isShowArchivedFilterOn = showArchived ?: isShowArchivedFilterOn
		return originalHabits
			.flattenAsObservable { it }
			.filter { habit -> habit.name.contains(query) && if (isShowArchivedFilterOn) true else !habit.isArchived }
			.toList()
			.flatMapCompletable {
				Completable.fromAction {
					filteredHabits.clear()
					filteredHabits.addAll(it)
				}
			}
	}

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
