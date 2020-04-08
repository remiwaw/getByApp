package com.rwawrzyniak.getby.habits.details

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.habits.Habit
import com.rwawrzyniak.getby.habits.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
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
	private val resources: Resources
) : HabitsDetailsViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()

	override fun observeState(): Observable<HabitDetailsViewState> {
		return Observable.just(HabitDetailsViewState(false))
	}

	override fun observeEffects(): Observable<HabitDetailsViewEffect> = effects.hide()

	override fun onAction(action: HabitDetailsViewAction): Completable {
		return when(action){
			is HabitDetailsViewAction.OnSaveHabitClicked -> validateAndSaveHabit(action.habit)
			is HabitDetailsViewAction.OnInputFieldStateChanged -> onInputFieldChanged(action)
		}
	}

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
			habitsRepository.saveHabit(habit)
				.andThen { effects.onNext(HabitDetailsViewEffect.DismissPopup) }
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

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}
}
