package com.rwawrzyniak.getby.habits.details

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.BaseFragment
import com.rwawrzyniak.getby.core.ChromeConfiguration
import com.rwawrzyniak.getby.core.SchedulerProvider
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
import java.util.ArrayList

class HabitDetailsFragment : BaseFragment() {
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

		binding.lineChart.setBackgroundColor(Color.WHITE)
		binding.lineChart.description.isEnabled = false
		binding.lineChart.setTouchEnabled(true)

		// binding.lineChart.setOnChartValueSelectedListener(this)
		binding.lineChart.setDrawGridBackground(false)
		binding.lineChart.isDragEnabled = true
		binding.lineChart.setScaleEnabled(true)
		binding.lineChart.setPinchZoom(true)

		var xAxis: XAxis = binding.lineChart.xAxis
		xAxis.enableGridDashedLine(10f, 10f, 0f)

		var yAxis: YAxis =  binding.lineChart.axisLeft
		yAxis.enableGridDashedLine(10f, 10f, 0f)
		yAxis.axisMaximum = 200f
		yAxis.axisMinimum = -50f

		binding.lineChart.axisRight.isEnabled = false

		return binding.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
		super.onViewCreated(view, savedInstanceState)
    }

	override fun onStart() {
		super.onStart()
		startObservers()
		habitId = requireNotNull(arguments?.getString(ARG_HABIT_ID))
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

			set1.setDrawIcons(false)
			set1.enableDashedLine(10f, 5f, 0f)

			set1.color = Color.BLACK
			set1.setCircleColor(Color.BLACK)

			set1.lineWidth = 1f
			set1.circleRadius = 3f

			set1.setDrawCircleHole(false)

			set1.formLineWidth = 1f
			set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
			set1.formSize = 15f

			set1.valueTextSize = 9f

			set1.enableDashedHighlightLine(10f, 5f, 0f)

			val dataSets = ArrayList<ILineDataSet>()
			dataSets.add(set1)

			val data = LineData(dataSets)
			lineChart.data = data
		}

		lineChart.invalidate()
	}

	private fun subscribeTo(completable: Completable) {
		completable.onErrorComplete()
			.subscribeOn(schedulerProvider.io())
			.subscribeBy(onError = Timber::e)
			.disposeBy(lifecycle.onStop)
	}

    companion object {
        const val ARG_HABIT_ID = "HabitIdArg"
    }
}
