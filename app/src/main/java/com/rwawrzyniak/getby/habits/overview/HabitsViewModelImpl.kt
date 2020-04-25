package com.rwawrzyniak.getby.habits.overview

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.dagger.BusModule.GLOBAL_EVENT_SUBJECT
import com.rwawrzyniak.getby.habits.persistance.Habit
import com.rwawrzyniak.getby.habits.persistance.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

abstract class HabitsViewModel: ViewModel() {
	abstract fun observeState(): Observable<HabitsViewState>
	abstract fun onAction(action: HabitsViewAction): Completable
}

class HabitsViewModelImpl @Inject internal constructor(
    @Named(GLOBAL_EVENT_SUBJECT) private val globalEventSubject: PublishSubject<GlobalEvent>,
    private val habitsRepository: HabitsRepository,
	private val dateTimeProvider: DateTimeProvider
) : HabitsViewModel() {
    private val compositeDisposable = CompositeDisposable()
	private val state = BehaviorSubject.create<HabitsViewState>()

	private val filteredHabits: MutableList<Habit> = arrayListOf()
	val oldFilteredHabits: MutableList<Habit> = arrayListOf()

	// TODO change this to use shared preferences
	private var isShowArchivedFilterOn = false

	override fun observeState(): Observable<HabitsViewState> = state.startWith(createDefaultState()).hide()

	override fun onAction(action: HabitsViewAction): Completable {
		return when(action){
			is HabitsViewAction.OnRemoveHabit -> onRemoveHabit(action.habit)
			is HabitsViewAction.OnArchiveHabit -> onArchiveHabit(action.habit)
			is HabitsViewAction.OnUpdateHabit -> onUpdateHabit(action.habit)
			is HabitsViewAction.OnTextFilterChanged -> onTextFilterChanged(action.filterText)
			is HabitsViewAction.OnShowArchiveChange -> onShowArchiveChange(action.isShowArchived)
		}
	}

	private fun onTextFilterChanged(filterText: String) = updateHabitState(filterText = filterText)
	private fun onShowArchiveChange(showArchived: Boolean) = updateHabitState(isShowArchived = showArchived)
	private fun onRemoveHabit(habit: Habit) = updateHabitState(removeHabit(habit))
	private fun onArchiveHabit(habit: Habit) = updateHabitState(archiveHabit(habit.copy(isArchived = true)))

	private fun onUpdateHabit(habit: Habit) = habitsRepository.updateHabit(habit) // no need to refresh view

	private fun updateHabitState(
		callback: Completable = Completable.complete(),
		isShowArchived: Boolean = false,
		filterText: String = ""
	): Completable =
		callback.andThen(
			refreshHabits(isShowArchived = isShowArchived, text = filterText)
				.flatMapCompletable { diffResult ->
					Completable.fromAction {
						updateState { viewState ->
							viewState.copy(
								habitsDiffResult = diffResult
							)
						}
					}
				})

	private fun createDefaultState(): Observable<HabitsViewState> {
		return Observables.combineLatest(initHabits(), Observable.just(false), initFirstHeaderDay()){
				habitsDiffResult: DiffUtil.DiffResult , isShowArchived: Boolean, firstHeaderDay ->
			HabitsViewState(habitsDiffResult, isShowArchived, firstHeaderDay)
		}
	}

	private fun initFirstHeaderDay() =
	globalEventSubject
		.startWith(GlobalEvent.DateChanged)
			.map {  dateTimeProvider.getCurrentDate() }

	private fun initHabits(): Observable<DiffUtil.DiffResult> =
		habitsRepository.loadHabits()
			.flattenAsObservable { it }
			.filter { !it.isArchived }
			.toList()
			.map { newHabits ->
				val diffResult = DiffUtil.calculateDiff(
				HabitDiffCallback(
					oldFilteredHabits,
					newHabits
				)
			)
				oldFilteredHabits.clear()
				oldFilteredHabits.addAll(newHabits)
				diffResult
			}.toObservable()

	private fun refreshHabits(text: CharSequence = "", isShowArchived: Boolean? = null): Single<DiffUtil.DiffResult> {
		return filter(text.toString(), isShowArchived)
			.andThen(Single.fromCallable {
				val diffResult =
					DiffUtil.calculateDiff(
						HabitDiffCallback(
							oldFilteredHabits,
							filteredHabits
						)
					)
				oldFilteredHabits.clear()
				oldFilteredHabits.addAll(filteredHabits)
				diffResult
			})
	}

	private fun removeHabit(habit: Habit): Completable = habitsRepository.removeHabit(habit)

	private fun archiveHabit(habit: Habit): Completable {
		habit.isArchived = true
		return updateHabit(habit)
	}

	private fun updateHabit(habit: Habit): Completable = habitsRepository.updateHabit(habit)

	private fun filter(query: String, showArchived: Boolean?): Completable {
		isShowArchivedFilterOn = showArchived ?: isShowArchivedFilterOn
		return habitsRepository.loadHabits()
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

	private fun updateState(callback: (HabitsViewState) -> HabitsViewState): Completable =
		state
			.take(1)
			.map(callback)
			.doOnNext { state.onNext(it) }
			.ignoreElements()

	override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
