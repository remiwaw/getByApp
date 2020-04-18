package com.rwawrzyniak.getby.habits.details

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.core.DateTimeProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.time.LocalDate
import javax.inject.Inject

// TODO add error handling
abstract class HabitDetailsViewModel: ViewModel() {
	abstract fun observeState(): Observable<HabitDetailsViewState>
	abstract fun observeEffects(): Observable<HabitDetailsViewEffect>
	abstract fun onAction(action: HabitDetailsViewAction): Completable
}


class HabitDetailsViewModelImpl @Inject constructor(
	private val resources: Resources,
	private val dateTimeProvider: DateTimeProvider,
	private val calculateHabitDayScoreUseCase: CalculateHabitDayScoreUseCase
) : HabitDetailsViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()
	private val state = BehaviorSubject.createDefault(createDefaultState())

	private fun createDefaultState(): HabitDetailsViewState = HabitDetailsViewState(emptyList())

	override fun observeState(): Observable<HabitDetailsViewState> = state.distinctUntilChanged().hide()

	override fun observeEffects(): Observable<HabitDetailsViewEffect> = effects.hide()

	override fun onAction(action: HabitDetailsViewAction): Completable {
		return when(action){
			is HabitDetailsViewAction.InitializeView -> updateLinearChartView(action.habitId)
			is HabitDetailsViewAction.OnSaveHabitClicked -> TODO()
			is HabitDetailsViewAction.OnInputFieldStateChanged -> TODO()
		}
	}

	private fun updateLinearChartView(habitId: String, fromDate: LocalDate = dateTimeProvider.getCurrentDate()): Completable {
		return calculateLinearChartEntries(
			habitId,
			fromDate
		).flatMapCompletable {
			Completable.fromAction { state.onNext(HabitDetailsViewState(it)) }
		}
	}

	private fun calculateLinearChartEntries(
		habitId: String,
		dateStart: LocalDate,
		daysToShow: Long = 7
	): Single<MutableList<Entry>> {
		return calculateHabitDayScoreUseCase.calculateScoreForDayRange(
			habitId,
			dateStart.minusDays(daysToShow),
			dateStart
		).flattenAsObservable { it }
			.map { Entry(it.date.dayOfMonth.toFloat(), it.score.toFloat()) }
			.toList()
	}

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}

}
