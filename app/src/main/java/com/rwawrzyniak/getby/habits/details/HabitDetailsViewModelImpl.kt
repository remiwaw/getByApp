package com.rwawrzyniak.getby.habits.details

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.habits.Habit
import com.rwawrzyniak.getby.habits.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
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
	private val calculateHabitDayScoreUseCase: CalculateHabitDayScoreUseCase,
	private val habitsRepository: HabitsRepository,
	private val calculateHistoryCalendarState: CalculateHistoryCalendarState

) : HabitDetailsViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()
	private val state = BehaviorSubject.createDefault(createDefaultState())

	private fun createDefaultState(): HabitDetailsViewState = HabitDetailsViewState(emptyList())

	override fun observeState(): Observable<HabitDetailsViewState> = state.distinctUntilChanged().hide()

	override fun observeEffects(): Observable<HabitDetailsViewEffect> = effects.hide()

	override fun onAction(action: HabitDetailsViewAction): Completable {
		return when(action){
			is HabitDetailsViewAction.InitializeView -> initializeView(action.habitId)
			is HabitDetailsViewAction.OnSaveHabitClicked -> TODO()
			is HabitDetailsViewAction.OnInputFieldStateChanged -> TODO()
			is HabitDetailsViewAction.LowestVisibleXBecomesVisible -> updateLinearChartViewOnScroll(action.firstVisibleEpochDay)
		}
	}

	private fun updateLinearChartViewOnScroll(firstVisibleEpochDay: Int): Completable {
		if (firstVisibleEpochDay == 0)
			return Completable.complete()

		return calculateLinearChartEntries(
			requireNotNull(state.value?.habit),
			dateTimeProvider.convertEpochToLocalDate(firstVisibleEpochDay.toLong())
		).flatMapCompletable { newEntries ->
			updateState { viewState ->
				viewState.copy(
					linearChartEntries = newEntries + viewState.linearChartEntries,
					historyCalendarState = viewState.historyCalendarState?.copy( isChanged =  false)
				)
			}
		}
	}

	private fun initializeView(
		habitId: String,
		fromDate: LocalDate = dateTimeProvider.getCurrentDate()
	): Completable = getHabitFromCacheOrRepo(habitId)
		.flatMapCompletable { habit ->
			Singles.zip(calculateLinearChartEntries(habit, fromDate), calculateHistoryCalendarState.calculate(habit))
				.flatMapCompletable { calendarHistoryStateAndlinearChartEntries ->
					Completable.fromAction {
						publishInitState(calendarHistoryStateAndlinearChartEntries, habit)
					}
				}
		}

	private fun publishInitState(
		calendarHistoryStateAndlinearChartEntries: Pair<List<Entry>, HistoryCalendarState>,
		habit: Habit
	) {
		state.onNext(
			HabitDetailsViewState(
				calendarHistoryStateAndlinearChartEntries.first,
				habit,
				habit.name,
				frequencyText(habit),
				reminderText(habit),
				historyCalendarState = calendarHistoryStateAndlinearChartEntries.second
			)
		)
	}

	private fun frequencyText(habit: Habit): String = resources.getString(
		R.string.frequencyTextInDetails,
		habit.frequency.times,
		habit.frequency.cycle
	)

	private fun reminderText(habit: Habit) =
		if (habit.reminder == null) resources.getString(R.string.reminderDefaultValue) else habit.reminder.toString()

	private fun getHabitFromCacheOrRepo(habitId: String): Single<Habit> =
		if (state.value?.habit != null) Single.just(requireNotNull((state.value as HabitDetailsViewState).habit))
		else habitsRepository.getSingle(habitId)

	private fun calculateLinearChartEntries(
		habit: Habit,
		dateStart: LocalDate,
		daysToLoad: Long = PAST_DAYS_TO_LOAD
	): Single<List<Entry>> {
		return calculateHabitDayScoreUseCase.calculateScoreForDayRangeExcludingStart(
			habit,
			dateStart.minusDays(daysToLoad),
			dateStart
		).flattenAsObservable { it }
			.map { Entry(it.date.toEpochDay().toFloat(), it.fulfilledPercentage.toFloat()) }
			.toList()
	}

	private fun updateState(callback: (HabitDetailsViewState) -> HabitDetailsViewState): Completable =
		state
			.take(1)
			.map(callback)
			.doOnNext { state.onNext(it) }
			.ignoreElements()

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}

	companion object{
		const val PAST_DAYS_TO_LOAD = 14L
	}

}
