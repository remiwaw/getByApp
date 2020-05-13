package com.rwawrzyniak.getby.habits.ui.createupdate

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.rwawrzyniak.getby.core.android.dagger.BusModule
import com.rwawrzyniak.getby.core.android.broadcast.MenuItemClickedEvent
import com.rwawrzyniak.getby.habits.R
import com.rwawrzyniak.getby.models.Frequency
import com.rwawrzyniak.getby.models.HabitModel
import com.rwawrzyniak.getby.repository.database.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

// TODO add error handling
abstract class HabitsCreateUpdateViewModel : ViewModel() {
	abstract fun observeState(): Observable<HabitCreateUpdateViewState>
	abstract fun observeEffects(): Observable<HabitDetailsViewEffect>
	abstract fun onAction(action: HabitCreateUpdateViewAction): Completable
}


class HabitsCreateUpdateViewModelImpl @Inject constructor(
	private val habitsRepository: HabitsRepository,
	private val resources: Resources,
	@Named(BusModule.MENU_ITEM_CLICKED_SUBJECT) private val globalEventSubject: PublishSubject<MenuItemClickedEvent>
) : HabitsCreateUpdateViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()
	private val state = BehaviorSubject.createDefault(
		HabitCreateUpdateViewState(
			false
		)
	)

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}

	override fun observeState(): Observable<HabitCreateUpdateViewState> = state.hide()

	override fun observeEffects(): Observable<HabitDetailsViewEffect> {
		return effects.mergeWith(globalEventSubject.map {
			when(it){
				MenuItemClickedEvent.SaveClicked -> HabitDetailsViewEffect.OnMenuSavedClicked
				else -> HabitDetailsViewEffect.DoNothing
			}
		}.hide())
	}

	override fun onAction(action: HabitCreateUpdateViewAction): Completable {
		return when(action){
			is HabitCreateUpdateViewAction.OnSaveHabitClicked -> validateAndSaveHabit(action.habit)
			is HabitCreateUpdateViewAction.OnInputFieldStateChanged -> onInputFieldChanged(action)
			is HabitCreateUpdateViewAction.LoadHabit -> loadHabit(action.habitId)
		}
	}

	private fun loadHabit(habitId: String): Completable {
		return habitsRepository.getById(habitId).flatMapCompletable {
			Completable.fromAction { state.onNext(
				HabitCreateUpdateViewState(
					true,
					it as HabitModel
				)
			) }
		}
	}

	private fun onInputFieldChanged(action: HabitCreateUpdateViewAction.OnInputFieldStateChanged): Completable =
		Completable.fromAction {
			effects.onNext(
				HabitDetailsViewEffect.ConfigureFields(
					InputFieldState(
						isError = action.isNameFieldEmpty
					),
					InputFieldState(
						isError = action.isDescriptionFieldEmpty
					)
				)
			)
		}

	private fun validateAndSaveHabit(habit: HabitModel): Completable {

		val effect =
			HabitDetailsViewEffect.ConfigureFields(
				habitNameInput = checkInputState(habit.name),
				habitDescriptionInput = checkInputState(habit.description),
				frequencyInput = checkFrequencyState(habit.frequency)
			)

		return if(effect.habitNameInput.isError || effect.habitDescriptionInput.isError || effect.frequencyInput.isError){
			Completable.fromAction {  effects.onNext(effect) }
		} else {
			habitsRepository.update(habit)
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
