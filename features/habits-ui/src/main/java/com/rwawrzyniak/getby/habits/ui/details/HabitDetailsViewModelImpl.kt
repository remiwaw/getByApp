package com.rwawrzyniak.getby.habits.ui.details

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.rwawrzyniak.getby.core.DateTimeProvider
import com.rwawrzyniak.getby.core.android.broadcast.MenuItemClickedEvent
import com.rwawrzyniak.getby.core.android.dagger.BusModule
import com.rwawrzyniak.getby.core.ext.toLocalDate
import com.rwawrzyniak.getby.habits.R
import com.rwawrzyniak.getby.entities.Habit
import com.rwawrzyniak.getby.entities.HabitDay
import com.rwawrzyniak.getby.repository.HabitsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

// TODO add error handling
abstract class HabitDetailsViewModel: ViewModel() {
	abstract fun observeState(): Observable<HabitDetailsViewState>
	abstract fun observeEffects(): Observable<HabitDetailsViewEffect>
	abstract fun onAction(action: HabitDetailsViewAction): Completable
}


class HabitDetailsViewModelImpl @Inject constructor(
	private val resources: Resources,
	private val dateTimeProvider: DateTimeProvider,
	private val calculateHabitDayScoreUseCase: com.rwawrzyniak.getby.habits.CalculateHabitDayScoreUseCase,
	private val habitsRepository: com.rwawrzyniak.getby.repository.HabitsRepository,
	private val calculateHistoryCalendarState: CalculateHistoryCalendarState,
	private val prepareBestSeriesForChart: PrepareBestSeriesForChart,
	@Named(BusModule.MENU_ITEM_CLICKED_SUBJECT) private val globalEventSubject: PublishSubject<MenuItemClickedEvent>
) : HabitDetailsViewModel() {
	private val compositeDisposable = CompositeDisposable()
	private val effects: Subject<HabitDetailsViewEffect> = PublishSubject.create<HabitDetailsViewEffect>()
	private val state = BehaviorSubject.createDefault(createDefaultState())

	private fun createDefaultState(): HabitDetailsViewState =
		HabitDetailsViewState(emptyList())

	override fun observeState(): Observable<HabitDetailsViewState> = state.hide()

	override fun observeEffects(): Observable<HabitDetailsViewEffect> {
		return effects.mergeWith(globalEventSubject.map {
			when(it){
				MenuItemClickedEvent.EditClicked -> HabitDetailsViewEffect.OnMenuEditClicked
				else -> HabitDetailsViewEffect.DoNothing
			}
		}.hide())
	}

	override fun onAction(action: HabitDetailsViewAction): Completable {
		return when(action){
			is HabitDetailsViewAction.InitializeView -> initializeView(action.habitId)
			is HabitDetailsViewAction.LowestVisibleXBecomesVisible -> updateLinearChartViewOnScroll(action.firstVisibleEpochDay)
			is HabitDetailsViewAction.OnSaveCalendarClicked -> saveNewDaysAndUpdateView(action.selectedDates)
		}
	}

	private fun saveNewDaysAndUpdateView(selectedDates: List<Date>): Completable = state.take(1)
		.flatMapCompletable {
			val newHistory: List<com.rwawrzyniak.getby.entities.HabitDay> = selectedDates.map { date ->
				com.rwawrzyniak.getby.entities.HabitDay(
					date.toLocalDate(),
					checked = true
				)
			}
			val updatedHabit = requireNotNull(it.habit).copy(history = newHistory)
			habitsRepository.saveHabit(updatedHabit)
				.andThen(initializeView(it.habit.id))
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
					historyCalendarState = viewState.historyCalendarState?.copy( isSelectedDatesChanged =  false)
				)
			}
		}
	}

	private fun initializeView(
		habitId: String,
		fromDate: LocalDate = dateTimeProvider.getCurrentDate()
	): Completable =
		habitsRepository.getSingle(habitId)
			.flatMapCompletable { habit ->
				Singles.zip(calculateLinearChartEntries(habit, fromDate), calculateHistoryCalendarState.calculate(habit), calculateBestStrikeChartEntries(habit)){
					linearChartEntries,calendarEntries, bestStrikeEntries ->
					ChartInfos(
						linearChartEntries,
						calendarEntries,
						bestStrikeEntries
					)
				}
					.flatMapCompletable { chartInfos ->
						Completable.fromAction {
							publishInitState(chartInfos, habit)
						}
					}
		}

	private fun publishInitState(
		chartInfos: ChartInfos,
		habit: com.rwawrzyniak.getby.entities.Habit
	) {
		state.onNext(
			HabitDetailsViewState(
				chartInfos.habitsHistoryEntries,
				chartInfos.bestStrikeEntries,
				chartInfos.historyCalendarState,
				habit.name,
				frequencyText(habit),
				reminderText(habit),
				habit
			)
		)
	}

	private fun frequencyText(habit: com.rwawrzyniak.getby.entities.Habit): String = resources.getString(
		R.string.frequencyTextInDetails,
		habit.frequency.times,
		habit.frequency.cycle
	)

	private fun reminderText(habit: com.rwawrzyniak.getby.entities.Habit) =
		if (habit.reminder == null) resources.getString(R.string.reminderDefaultValue) else habit.reminder.toString()

	private fun calculateBestStrikeChartEntries(habit: com.rwawrzyniak.getby.entities.Habit): Single<MutableList<BarEntry>> =
		prepareBestSeriesForChart.mapStrikesToBarEntry(habit)

	private fun calculateLinearChartEntries(
		habit: com.rwawrzyniak.getby.entities.Habit,
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

	private data class ChartInfos(val habitsHistoryEntries: List<Entry>, val historyCalendarState: HistoryCalendarState, val bestStrikeEntries: List<BarEntry>)

	companion object{
		const val PAST_DAYS_TO_LOAD = 14L
	}

}
