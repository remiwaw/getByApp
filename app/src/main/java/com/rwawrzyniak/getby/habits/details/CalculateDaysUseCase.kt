package com.rwawrzyniak.getby.habits.details

import com.rwawrzyniak.getby.core.ext.date.toDate
import com.rwawrzyniak.getby.habits.Habit
import io.reactivex.Single
import javax.inject.Inject

class CalculateDaysUseCase @Inject internal constructor() {
	fun filterByCheckStatus(
		habit: Habit,
		isChecked: Boolean = true
	): Single<MutableList<java.util.Date>> =
		Single.just(habit)
			.flattenAsObservable{ it.history }
			.filter { it.checked == isChecked }
			.map { it.day.toDate() }
			.toList()

}
