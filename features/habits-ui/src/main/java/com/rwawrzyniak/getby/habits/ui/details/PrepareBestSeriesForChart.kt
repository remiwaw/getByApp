package com.rwawrzyniak.getby.habits.ui.details

import com.github.mikephil.charting.data.BarEntry
import com.rwawrzyniak.getby.habits.Strike
import com.rwawrzyniak.getby.models.HabitModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith

class PrepareBestSeriesForChart (private val calculateBestSeriesUseCase: com.rwawrzyniak.getby.habits.CalculateBestSeriesUseCase) {

	fun mapStrikesToBarEntry(
		habitModel: HabitModel
	): Single<MutableList<BarEntry>> =
		calculateBestSeriesUseCase.calculateStrike(habitModel)
			.flattenAsObservable{ it }
			.take(MAX_BEST_STRIKES_TO_BE_DISPLAYED)
			.zipWith(Observable.range(0, Integer.MAX_VALUE))
			.map { strikeWithIndex: Pair<Strike, Int> ->
				val index = strikeWithIndex.second
				val strike = strikeWithIndex.first
				val daysInRow = strike.daysInRow
				BarEntry(index.toFloat(), floatArrayOf((-daysInRow/2f), daysInRow/2f), strike)
			}
			.toList()

	companion object {
		private const val MAX_BEST_STRIKES_TO_BE_DISPLAYED = 3L
	}
}
