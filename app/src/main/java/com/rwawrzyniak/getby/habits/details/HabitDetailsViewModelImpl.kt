package com.rwawrzyniak.getby.habits.details

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.habits.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

// TODO add error handling
abstract class HabitDetailsViewModel: ViewModel() {
	abstract fun observeState(): Observable<HabitDetailsViewState>
	abstract fun observeEffects(): Observable<HabitDetailsViewEffect>
	abstract fun onAction(action: HabitDetailsViewAction): Completable
}


class HabitDetailsViewModelImpl @Inject constructor(
    private val habitsRepository: HabitsRepository,
	private val resources: Resources,
	private val dateTimeProvider: DateTimeProvider,
	private val calculateHabitDayScoreUseCase: CalculateHabitDayScoreUseCase
) : HabitDetailsViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()
	private val state = BehaviorSubject.createDefault(createDefaultState())

	private fun createDefaultState(): HabitDetailsViewState {
		return HabitDetailsViewState(
			linearChartEntries = calculateLinearChartEntries()
		)
	}

	private fun calculateLinearChartEntries(): List<Entry> {
		// calculateHabitDayScoreUseCase.calculateScoreForDay()
		return (0 until 10).map { index ->
			Entry(
				index.toFloat(),
				(index * 10).toFloat()
			)
		}
	}

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}

	override fun observeState(): Observable<HabitDetailsViewState> = state.hide()

	override fun observeEffects(): Observable<HabitDetailsViewEffect> = effects.hide()

	override fun onAction(action: HabitDetailsViewAction): Completable {
		return when(action){
			is HabitDetailsViewAction.LoadHabit -> TODO()
			is HabitDetailsViewAction.OnSaveHabitClicked -> TODO()
			is HabitDetailsViewAction.OnInputFieldStateChanged -> TODO()
		}
	}
}
