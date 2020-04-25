package com.rwawrzyniak.getby.habits.overview

import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.dagger.BusModule.GLOBAL_EVENT_SUBJECT
import com.rwawrzyniak.getby.dagger.SchedulerModule.SCHEDULER_PROVIDER
import com.rwawrzyniak.getby.habits.persistance.Habit
import com.rwawrzyniak.getby.habits.persistance.HabitsRepository
import com.rwawrzyniak.getby.habits.details.HabitDetailsViewAction
import com.rwawrzyniak.getby.habits.details.HabitDetailsViewEffect
import com.rwawrzyniak.getby.habits.details.HabitDetailsViewState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named
abstract class HabitsViewModel: ViewModel() {
	abstract fun observeState(): Observable<HabitDetailsViewState>
	abstract fun observeEffects(): Observable<HabitDetailsViewEffect>
	abstract fun onAction(action: HabitDetailsViewAction): Completable
}

class HabitsViewModelImpl @Inject internal constructor(
    @Named(GLOBAL_EVENT_SUBJECT) private val globalEventSubject: PublishSubject<GlobalEvent>,
    @Named(SCHEDULER_PROVIDER) private val schedulerProvider: SchedulerProvider,
    private val habitsRepository: HabitsRepository,
	private val dateTimeProvider: DateTimeProvider,
	private val headerDaySubject: BehaviorSubject<LocalDate> = BehaviorSubject.createDefault(dateTimeProvider.getCurrentDate())
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    // Observers will subscribe to this since it is immutable to them
    val originalHabits: Single<List<Habit>> = habitsRepository.loadHabits()
	val filteredHabits: MutableList<Habit> = arrayListOf()
	val oldFiteredHabits: MutableList<Habit> = arrayListOf()

	private var isShowArchivedFilterOn = false


    init {
		globalEventSubject
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribeBy ( onNext =  headerDaySubject.onNext(dateTimeProvider.getCurrentDate()))
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
