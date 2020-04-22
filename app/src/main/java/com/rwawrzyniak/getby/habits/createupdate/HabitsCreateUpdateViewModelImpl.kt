package com.rwawrzyniak.getby.habits.createupdate

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.habits.Frequency
import com.rwawrzyniak.getby.habits.Habit
import com.rwawrzyniak.getby.habits.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

// TODO add error handling
abstract class HabitsCreateUpdateViewModel : ViewModel() {
	abstract fun observeState(): Observable<HabitCreateUpdateViewState>
	abstract fun observeEffects(): Observable<HabitDetailsViewEffect>
	abstract fun onAction(action: HabitCreateUpdateViewAction): Completable
}


class HabitsCreateUpdateViewModelImpl @Inject constructor(
	private val habitsRepository: HabitsRepository,
	private val resources: Resources
) : HabitsCreateUpdateViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()
	private val state = BehaviorSubject.createDefault(HabitCreateUpdateViewState(false))

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}

	override fun observeState(): Observable<HabitCreateUpdateViewState> = state.hide()

	override fun observeEffects(): Observable<HabitDetailsViewEffect> = effects.hide()

	override fun onAction(action: HabitCreateUpdateViewAction): Completable {
		return when(action){
			is HabitCreateUpdateViewAction.OnSaveHabitClicked -> validateAndSaveHabit(action.habit)
			is HabitCreateUpdateViewAction.OnInputFieldStateChanged -> onInputFieldChanged(action)
			is HabitCreateUpdateViewAction.LoadHabit -> loadHabit(action.habitId)
		}
	}

	private fun loadHabit(habitId: String): Completable {
		return habitsRepository.getSingle(habitId).flatMapCompletable {
			Completable.fromAction { state.onNext(HabitCreateUpdateViewState(true, it)) }
		}
	}

	private fun onInputFieldChanged(action: HabitCreateUpdateViewAction.OnInputFieldStateChanged): Completable =
		Completable.fromAction {
			effects.onNext(HabitDetailsViewEffect.ConfigureFields(
				InputFieldState(isError = action.isNameFieldEmpty),
				InputFieldState(isError = action.isDescriptionFieldEmpty)
			))
		}

	private fun validateAndSaveHabit(habit: Habit): Completable {

		val effect = HabitDetailsViewEffect.ConfigureFields(
			habitNameInput = checkInputState(habit.name),
			habitDescriptionInput = checkInputState(habit.description),
			frequencyInput = checkFrequencyState(habit.frequency)
		)

		return if(effect.habitNameInput.isError || effect.habitDescriptionInput.isError || effect.frequencyInput.isError){
			Completable.fromAction {  effects.onNext(effect) }
		} else {
			habitsRepository.saveHabit(habit)
				.andThen { effects.onNext(HabitDetailsViewEffect.GoBack) }
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

	private fun checkFrequencyState(frequency: Frequency): InputFieldState =
		if (frequency.times > frequency.cycle) {
			InputFieldState(
				isEnabled = true,
				isError = true,
				errorMessage = resources.getString(R.string.frequencyIncorrect)
			)
		} else {
			InputFieldState()
		}
}
