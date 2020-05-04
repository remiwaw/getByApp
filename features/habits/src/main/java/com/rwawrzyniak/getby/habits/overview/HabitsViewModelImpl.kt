package com.rwawrzyniak.getby.habits.overview

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.rwawrzyniak.getby.common.AppPreferences
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.android.broadcast.BusModule.GLOBAL_EVENT_SUBJECT
import com.rwawrzyniak.getby.core.android.broadcast.GlobalEvent
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

// TODO check if it possible to simply filter method / init methods
abstract class HabitsViewModel: ViewModel() {
	abstract fun observeState(): Observable<HabitsViewState>
	abstract fun onAction(action: HabitsViewAction): Completable
}

class HabitsViewModelImpl @Inject internal constructor(
    @Named(GLOBAL_EVENT_SUBJECT) private val globalEventSubject: PublishSubject<GlobalEvent>,
    private val habitsRepository: HabitsRepository,
	private val dateTimeProvider: DateTimeProvider,
	private val preferences: AppPreferences
) : HabitsViewModel() {
    private val compositeDisposable = CompositeDisposable()
	private val state = BehaviorSubject.create<HabitsViewState>()

	private val filteredHabits: MutableList<Habit> = arrayListOf()
	private val oldFilteredHabits: MutableList<Habit> = arrayListOf()

	override fun observeState(): Observable<HabitsViewState> = state.hide()

	override fun onAction(action: HabitsViewAction): Completable {
		return when(action){
			is HabitsViewAction.OnRemoveHabit -> onRemoveHabit(action.habit)
			is HabitsViewAction.OnSwitchArchiveState -> switchArchiveState(action.habit)
			is HabitsViewAction.OnUpdateHabit -> onUpdateHabit(action.habit)
			is HabitsViewAction.OnTextFilterChanged -> onTextFilterChanged(action.filterText)
			is HabitsViewAction.OnShowArchiveChange -> onShowArchiveChange(action.isHideArchived)
			is HabitsViewAction.Init -> createDefaultState().flatMapCompletable { Completable.fromAction { state.onNext(it) }  }
		}
	}

	private fun onTextFilterChanged(filterText: String) = updateHabitState(filterText = filterText)
	private fun onShowArchiveChange(hideArchived: Boolean): Completable {
		val callback = Completable.fromAction { preferences.setHideArchivedHabits(hideArchived) }
		return updateHabitState(callback)
	}
	private fun onRemoveHabit(habit: Habit) = updateHabitState(removeHabit(habit))
	private fun switchArchiveState(habit: Habit) = updateHabitState(updateHabit(habit.copy(isArchived = habit.isArchived.not())))

	private fun onUpdateHabit(habit: Habit) = habitsRepository.updateHabit(habit) // no need to refresh view

	private fun updateHabitState(
		callback: Completable = Completable.complete(),
		filterText: String = ""
	): Completable {
		return callback.andThen(
			Completable.defer {
				refreshHabits(text = filterText)
					.flatMapCompletable { updatedHabitsInfo ->
						updateState { viewState ->
							viewState.copy(
								isInit = false,
								updatedHabitsInfo = updatedHabitsInfo,
								isHideArchive = preferences.getHideArchivedHabits()
							)
						}
					}
			}
		)
	}

	private fun createDefaultState(): Observable<HabitsViewState> {
		return Observables.combineLatest(
			initHabits(preferences.getHideArchivedHabits()),
			initFirstHeaderDay()
		){
				updatedHabitsInfo: UpdatedHabitsInfo, firstHeaderDay ->
			HabitsViewState(
				updatedHabitsInfo,
				preferences.getHideArchivedHabits(),
				true,
				firstHeaderDay
			)
		}
	}

	private fun initFirstHeaderDay() =
	globalEventSubject
		.startWith(GlobalEvent.DateChanged)
			.map {  dateTimeProvider.getCurrentDate() }

	private fun initHabits(hideArchived: Boolean): Observable<UpdatedHabitsInfo> =
		habitsRepository.loadHabits()
			.flattenAsObservable { it }
			.filter { if (hideArchived) !it.isArchived else true  }
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
				UpdatedHabitsInfo(newHabits, diffResult)
			}.toObservable()

	private fun refreshHabits(text: CharSequence = ""): Single<UpdatedHabitsInfo> =
		Single.just(preferences.getHideArchivedHabits())
			.flatMapCompletable { hideArchived -> filter(text.toString(), hideArchived)}
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
				UpdatedHabitsInfo(filteredHabits, diffResult)
			})

	private fun removeHabit(habit: Habit): Completable = habitsRepository.removeHabit(habit)
	private fun updateHabit(habit: Habit): Completable = habitsRepository.updateHabit(habit)

	private fun filter(query: String, hideArchived: Boolean): Completable {
		return habitsRepository.loadHabits()
			.flattenAsObservable { it }
			.filter { habit -> habit.name.contains(query) && if (hideArchived) !habit.isArchived else true  }
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
