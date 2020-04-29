package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.habits.persistance.Habit
import io.reactivex.Single
import java.time.LocalDate
import javax.inject.Inject

class CalculateBestSeriesUseCase @Inject internal constructor() {
	fun calculateScoreForDayRangeExcludingStart(
		habit: Habit
	): Single<List<Strike>> =
		Single.just(habit)
			.flattenAsObservable { it.history }
			.filter { it.checked }
			.map { it.day }
			.toList()
			.map { findTheLongestStrike(it) }

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
				if(i == dateList.size-1 && prev != firstInStrike) {
					strikes.add(Strike(firstInStrike, next, count+1))
					count = 0
				}
			} else {
				newStrikeFound = true
				if(prev != firstInStrike){
					strikes.add(Strike(firstInStrike, prev, count+1))
					count = 0
				}
			}
			prev = next
		}
		return strikes.toList()
	}
}
