package com.rwawrzyniak.getby.habits

import com.rwawrzyniak.getby.models.HabitModel
import io.reactivex.Single
import java.time.LocalDate
import kotlin.collections.first
import kotlin.collections.sortedByDescending
import kotlin.collections.toList
import kotlin.ranges.until

class CalculateBestSeriesUseCase {

	fun calculateStrike(
		habit: HabitModel
	): Single<List<Strike>> =
		Single.just(habit)
			.flattenAsObservable { it.history }
			.filter { it.checked }
			.map { it.day }
			.toList()
			.map { findTheLongestStrike(it).sortedByDescending { strike -> strike.daysInRow } }


	private fun findTheLongestStrike(
		dateList: List<LocalDate>
	): List<Strike> {
		if(dateList.isEmpty()){
			return emptyList()
		}
		if(dateList.size == 1){
			return listOf(Strike(dateList[0], dateList[0], 1))
		}

		var count = 0
		var prev = dateList[0]
		var firstInStrike: LocalDate = dateList.first()
		val strikes = mutableListOf<Strike>()
		var newStrikeFound = false

		for (i in 1 until dateList.size) {
			val next = dateList[i]

			if(newStrikeFound){
				firstInStrike = prev
				newStrikeFound = false
			}

			if (prev.plusDays(1) == next) {
				count+=1
			} else if(count > 0){
				strikes.add(Strike(firstInStrike, prev, count+1))
				count = 0
				newStrikeFound = true
			}

			prev = next

			if(i == dateList.size-1 && count > 0) {
				strikes.add(Strike(firstInStrike, prev, count+1))
			}
		}
		return strikes.toList()
	}
}
