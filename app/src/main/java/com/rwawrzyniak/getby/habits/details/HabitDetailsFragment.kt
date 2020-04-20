package com.rwawrzyniak.getby.habits.details

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.core.ChromeConfiguration
import com.rwawrzyniak.getby.core.SchedulerProvider
import com.rwawrzyniak.getby.core.ext.date.toShortForm
import com.rwawrzyniak.getby.dagger.fragmentScopedViewModel
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.FragmentHabitDetailsBinding
import io.reactivex.Completable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_habit_details.*
import timber.log.Timber
import java.time.LocalDate
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class HabitDetailsFragment : BaseFragment(), OnChartGestureListener {
	private lateinit var binding: FragmentHabitDetailsBinding
	private val viewModel by fragmentScopedViewModel { injector.habitDetailsViewModel }
	private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }
	private lateinit var habitId: String
	private var isDragInProgressSubject = PublishSubject.create<Boolean>()
	private var noMoreDataToDisplaySubject = PublishSubject.create<Boolean>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentHabitDetailsBinding.inflate(inflater, container, false)

		setupLinearChart()

		return binding.root
	}

	override fun onStart() {
		super.onStart()
		startObservers()
		habitId = requireNotNull(arguments?.getString(ARG_HABIT_ID))
		subscribeTo(viewModel.onAction(HabitDetailsViewAction.InitializeView(habitId)))
	}

	private fun startObservers() {
		wireViewModel()
	}

	override fun getChromeConfig(): ChromeConfiguration {
		return ChromeConfiguration(
			showActionBar = true,
			actionBarTitle = getString(R.string.habitsDetailsTitle),
			showActionBarEditButton = true,
			showActionBarSaveButton = false
		)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when(item.itemId){
			R.id.action_edit -> nav_host.findNavController().navigate(
				R.id.action_habitDetailsFragment_to_habitCreateUpdateFragment,
				Bundle().apply {
					putString(ARG_HABIT_ID ,habitId)
				}
			)
		}
		return super.onOptionsItemSelected(item)
	}

	private fun wireViewModel() {
		viewModel.observeState()
			.observeOn(schedulerProvider.main())
			.subscribeBy(onNext = this::renderState)
			.disposeBy(lifecycle.onStop)

		viewModel.observeEffects()
			.observeOn(schedulerProvider.main())
			.subscribeBy(onNext = this::executeEffect)
			.disposeBy(lifecycle.onStop)

		Observables.combineLatest(isDragInProgressSubject.hide(), noMoreDataToDisplaySubject.hide()){
				isDragInProgress, noMoreDataToDisplay -> isDragInProgress.not() && noMoreDataToDisplay

		}.distinctUntilChanged()
			.subscribeOn(schedulerProvider.io())
			.observeOn(schedulerProvider.main())
			.subscribeBy(onNext = { shouldRefreshData -> if(shouldRefreshData) refreshData() })
			.disposeBy(onStop)
	}

	private fun executeEffect(effect: HabitDetailsViewEffect) {
	}

	private fun renderState(state: HabitDetailsViewState) {
		habitDetaislName.text = state.habitName
		habitDetailsFrequency.text = state.frequencyText
		habitDetailsAlarm.text = state.habitAlarmText

		if(state.linearChartEntries.isEmpty())
			return

		renderLinearChart(state)
	}

	private fun setupLinearChart() {
		binding.lineChart.setBackgroundColor(Color.WHITE)
		binding.lineChart.description.isEnabled = false
		binding.lineChart.setTouchEnabled(true)

		binding.lineChart.setDrawGridBackground(false)

		binding.lineChart.isDragYEnabled = true
		binding.lineChart.onChartGestureListener = this
		binding.lineChart.legend.isEnabled = false
		binding.lineChart.extraBottomOffset = 15f


		var xAxis: XAxis = binding.lineChart.xAxis
		xAxis.position = XAxis.XAxisPosition.BOTTOM
		xAxis.valueFormatter =
			IAxisValueFormatter { value, _ -> LocalDate.ofEpochDay(value.toLong()).toShortForm() }
		xAxis.labelRotationAngle = 60f

		var yAxis = binding.lineChart.axisLeft
		yAxis.valueFormatter = IAxisValueFormatter { value, _ -> "$value%" }
		yAxis.axisMaximum = 100f
		yAxis.axisMinimum = 0f

		binding.lineChart.axisRight.isEnabled = false
	}

	private fun renderLinearChart(state: HabitDetailsViewState) {
		val set1: LineDataSet
		binding.lineChart.isDragDecelerationEnabled = false

		if (lineChart.data != null &&
			lineChart.data.dataSetCount > 0
		) {
			val lastLowestVisibleX = binding.lineChart.lowestVisibleX

			set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
			set1.values = state.linearChartEntries
			set1.notifyDataSetChanged()

			lineChart.data.notifyDataChanged()
			lineChart.notifyDataSetChanged()

			// TODO For some reason scroll to x doesnt work withou delay.
			// https://github.com/PhilJay/MPAndroidChart/issues/765
			Completable
				.timer(100, TimeUnit.MILLISECONDS)
				.subscribeOn(schedulerProvider.io())
				.observeOn(schedulerProvider.main())
				.subscribe({
					binding.lineChart.moveViewToX((lastLowestVisibleX))
				}, {
					// do something on error
				}).disposeBy(onStop)

		} else {
			set1 = LineDataSet(state.linearChartEntries, "")

			set1.color = Color.CYAN
			set1.setCircleColor(Color.CYAN)

			set1.lineWidth = 1.75f
			set1.circleRadius = 5f
			set1.circleHoleRadius = 2.5f
			set1.highLightColor = Color.WHITE

			set1.setDrawCircleHole(false)
			set1.valueTextSize = 9f
			set1.valueTextColor = Color.BLUE
			set1.valueFormatter = PercentFormatter()

			val dataSets = ArrayList<ILineDataSet>()
			dataSets.add(set1)

			val data = LineData(dataSets)
			lineChart.data = data

			binding.lineChart.moveViewToX(binding.lineChart.xAxis.axisMaximum)
		}

		binding.lineChart.setVisibleXRangeMaximum(VISIBLE_X_RANGE)
	}

	private fun subscribeTo(completable: Completable) {
		completable.onErrorComplete()
			.subscribeOn(schedulerProvider.io())
			.subscribeBy(onError = Timber::e)
			.disposeBy(lifecycle.onStop)
	}

	override fun onChartGestureStart(
		me: MotionEvent,
		lastPerformedGesture: ChartGesture?
	) {
		if(lastPerformedGesture == ChartGesture.DRAG){
			isDragInProgressSubject.onNext(true)
		}
		Log.i("Gesture", "START, x: " + me.x + ", y: " + me.y)
	}

	override fun onChartGestureEnd(
		me: MotionEvent?,
		lastPerformedGesture: ChartGesture
	) {
		if(lastPerformedGesture == ChartGesture.DRAG){
			isDragInProgressSubject.onNext(false)
		}
		Log.i("Gesture", "END, lastGesture: $lastPerformedGesture")
	}

	override fun onChartLongPressed(me: MotionEvent?) {
		Log.i("LongPress", "Chart longpressed.")
	}

	override fun onChartDoubleTapped(me: MotionEvent?) {
		Log.i("DoubleTap", "Chart double-tapped.")
	}

	override fun onChartSingleTapped(me: MotionEvent?) {
		Log.i("SingleTap", "Chart single-tapped.")
	}

	override fun onChartFling(
		me1: MotionEvent?, me2: MotionEvent?,
		velocityX: Float, velocityY: Float
	) {
		Log.i(
			"Fling", "Chart flinged. VeloX: "
				+ velocityX + ", VeloY: " + velocityY
		)
	}

	override fun onChartScale(
		me: MotionEvent?,
		scaleX: Float,
		scaleY: Float
	) {
		Log.i("Scale / Zoom", "ScaleX: $scaleX, ScaleY: $scaleY")
	}

	override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {
		if(dX > 0 && binding.lineChart.xAxis.axisMinimum == binding.lineChart.lowestVisibleX){
			noMoreDataToDisplaySubject.onNext(true)
		}
	}

	private fun refreshData(){
		subscribeTo(viewModel.onAction(HabitDetailsViewAction.LowestVisibleXBecomesVisible(binding.lineChart.xAxis.axisMinimum.toInt())))
	}

	companion object {
		const val ARG_HABIT_ID = "HabitIdArg"
		const val VISIBLE_X_RANGE = 7f
	}
}
