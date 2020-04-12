package com.rwawrzyniak.getby.habits.details

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.date.datesInRangeFromToday
import com.rwawrzyniak.getby.date.getLastNDays
import com.rwawrzyniak.getby.habits.Habit
import com.rwawrzyniak.getby.habits.HabitDay
import com.rwawrzyniak.getby.habits.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

// TODO add error handling
abstract class HabitsDetailsViewModel : ViewModel() {
	abstract fun observeState(): Observable<HabitDetailsViewState>
	abstract fun observeEffects(): Observable<HabitDetailsViewEffect>
	abstract fun onAction(action: HabitDetailsViewAction): Completable
}


class HabitsDetailsViewModelImpl @Inject constructor(
    private val habitsRepository: HabitsRepository,
	private val resources: Resources,
	private val dateTimeProvider: DateTimeProvider
) : HabitsDetailsViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()
	private val state = BehaviorSubject.createDefault(HabitDetailsViewState(false))

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}

	override fun observeState(): Observable<HabitDetailsViewState> = state.hide()

	override fun observeEffects(): Observable<HabitDetailsViewEffect> = effects.hide()

	override fun onAction(action: HabitDetailsViewAction): Completable {
		return when(action){
			is HabitDetailsViewAction.OnSaveHabitClicked -> validateAndSaveHabit(action.habit)
			is HabitDetailsViewAction.OnInputFieldStateChanged -> onInputFieldChanged(action)
			is HabitDetailsViewAction.LoadHabit -> loadHabit(action.habitId)
		}
	}

	private fun loadHabit(habitId: String): Completable {
		return habitsRepository.getSingle(habitId).flatMapCompletable {
			Completable.fromAction { state.onNext(HabitDetailsViewState(true, it)) }
		}
	}
	// // private fun updateHabitState(habit: Habit): Completable =
	// // 	updateState { copy(configuration = config) }
	//
	// private fun updateState(callback: HabitDetailsViewState.() -> HabitDetailsViewState) =
	// 	state.take(1)
	// 		.map(callback)
	// 		.doOnNext { state.onNext(it) }
	// 		.ignoreElements()

	private fun onInputFieldChanged(action: HabitDetailsViewAction.OnInputFieldStateChanged): Completable =
		Completable.fromAction {
			effects.onNext(HabitDetailsViewEffect.ConfigureFields(
				InputFieldState(isError = action.isNameFieldEmpty),
				InputFieldState(isError = action.isDescriptionFieldEmpty)
			))
		}

	private fun validateAndSaveHabit(habit: Habit): Completable {

		val effect = HabitDetailsViewEffect.ConfigureFields(
			habitNameInput = checkInputState(habit.name),
			habitDescriptionInput = checkInputState(habit.description)
		)

		return if(effect.habitNameInput.isError || effect.habitDescriptionInput.isError){
			Completable.fromAction {  effects.onNext(effect) }
		} else {
			initializeHabitHistoryIfEmpty(habit)
			habitsRepository.saveHabit(habit)
				.andThen { effects.onNext(HabitDetailsViewEffect.DismissPopup) }
		}
	}

	private fun initializeHabitHistoryIfEmpty(habit: Habit) {
		if (habit.history.isEmpty()) {
			habit.history = initHistory()
		}
	}

	private fun checkInputState(inputTextValue: String): InputFieldState =
		if (inputTextValue.isBlank()) {
			InputFieldState(
				isEnabled = false,
				isError = true,
				errorMessage = resources.getString(R.string.empty_field_error)
			)
		} else {
			InputFieldState()
		}

	// TODO this could lead to problem if user has change his calendar settings. I.e manually set date
	// TODO Important find a better way to initialzie dates. After a year is passed this will cause indexBoundOf exception!
	private fun initHistory() = dateTimeProvider.getCurrentDate().datesInRangeFromToday(5, 365).mapIndexed {
			index, localDate -> HabitDay(localDate, index)
	}
}
