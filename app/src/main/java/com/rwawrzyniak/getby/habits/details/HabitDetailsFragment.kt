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
import com.github.mikephil.charting.highlight.Highlight
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
import io.reactivex.rxkotlin.subscribeBy
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_habit_details.*
import timber.log.Timber
import java.time.LocalDate
import java.util.ArrayList

class HabitDetailsFragment : BaseFragment(), OnChartGestureListener {
    private lateinit var binding: FragmentHabitDetailsBinding
    private val viewModel by fragmentScopedViewModel { injector.habitDetailsViewModel }
	private val schedulerProvider: SchedulerProvider by lazy { injector.provideSchedulerProvider() }
	private var isUserInput = true // TODO make it better, change to avoid executing listener on text changed.
	private lateinit var habitId: String

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

		// binding.lineChart.setOnChartValueSelectedListener(this)
		binding.lineChart.setDrawGridBackground(false)

		// enable dragging
		binding.lineChart.isDragYEnabled = true
		binding.lineChart.onChartGestureListener = this

		var xAxis: XAxis = binding.lineChart.xAxis
		xAxis.position = XAxis.XAxisPosition.BOTTOM
		xAxis.valueFormatter =
			IAxisValueFormatter { value, _ -> LocalDate.ofEpochDay(value.toLong()).toShortForm() }
		xAxis.labelRotationAngle = 60f
		// xAxis.enableGridDashedLine(10f, 10f, 0f)

		var yAxis = binding.lineChart.axisLeft
		yAxis.valueFormatter = IAxisValueFormatter { value, _ -> "$value%" }
		yAxis.axisMaximum = 100f
		yAxis.axisMinimum = 0f

		binding.lineChart.axisRight.isEnabled = false
	}

	private fun renderLinearChart(state: HabitDetailsViewState) {
		val set1: LineDataSet

		if (lineChart.data != null &&
			lineChart.data.dataSetCount > 0
		) {
			set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
			set1.values = state.linearChartEntries
			set1.notifyDataSetChanged()
			lineChart.data.notifyDataChanged()
			lineChart.notifyDataSetChanged()
		} else {
			set1 = LineDataSet(state.linearChartEntries, "DataSet 1")

			set1.color = Color.BLACK
			set1.setCircleColor(Color.BLACK)

			set1.lineWidth = 1f
			set1.circleRadius = 3f

			set1.setDrawCircleHole(false)

			set1.valueTextSize = 9f

			val dataSets = ArrayList<ILineDataSet>()
			dataSets.add(set1)

			val data = LineData(dataSets)
			lineChart.data = data
		}

		binding.lineChart.setVisibleXRangeMaximum(7f)
		binding.lineChart.moveViewToX(state.todayEpochDay.toFloat())
		// lineChart.invalidate()
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
		Log.i("Gesture", "START, x: " + me.x + ", y: " + me.y)
	}

	override fun onChartGestureEnd(
		me: MotionEvent?,
		lastPerformedGesture: ChartGesture
	) {
		Log.i("Gesture", "END, lastGesture: $lastPerformedGesture")
		// un-highlight values after the gesture is finished and no single-tap
		if (lastPerformedGesture != ChartGesture.SINGLE_TAP) // or highlightTouch(null) for callback to onNothingSelected(...)
			binding.lineChart.highlightValues(null)
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

	override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
		Log.i("Translate / Move", "dX: $dX, dY: $dY")
	}

	fun onValueSelected(
		e: Map.Entry<*, *>,
		dataSetIndex: Int,
		h: Highlight?
	) {
		Log.i("Entry selected", e.toString())
		Log.i(
			"LOWHIGH", "low: " + binding.lineChart.lowestVisibleX
				.toString() + ", high: " + binding.lineChart.highestVisibleX
		)
		Log.i(
			"MIN MAX", "xmin: " + binding.lineChart.xChartMin
				.toString() + ", xmax: " + binding.lineChart.xChartMax
				.toString() + ", ymin: " + binding.lineChart.yChartMin
				.toString() + ", ymax: " + binding.lineChart.yChartMax
		)
	}

	fun onNothingSelected() {
		Log.i("Nothing selected", "Nothing selected.")
	}

    companion object {
        const val ARG_HABIT_ID = "HabitIdArg"
    }
}
