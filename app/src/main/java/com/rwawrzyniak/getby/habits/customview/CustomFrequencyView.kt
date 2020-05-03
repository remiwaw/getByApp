package com.rwawrzyniak.getby.habits.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_frequency_element.view.*
import modularization.android.R

class CustomFrequencyView : ConstraintLayout {
	private var isUserAction = true

    init {
        inflate(context, R.layout.custom_frequency_element, this)
		setDefault()
	}

	private fun setDefault() {
		customFrequencyTimesEditText.setText(DEFAULT_TIMES_VALUE)
		customFrequencyDaysEditText.setText(DEFAULT_DAYS_VALUE)

		customFrequencyTimesEditText.setOnFocusChangeListener { _, hasFocus ->
			if(isUserAction && hasFocus.not() && isNotValid(customFrequencyTimesEditText.editableText.toString())) {
				customFrequencyTimesEditText.setText(DEFAULT_TIMES_VALUE)
			}
		}

		customFrequencyDaysEditText.setOnFocusChangeListener { _, hasFocus ->
			if(isUserAction && hasFocus.not() && isNotValid(customFrequencyDaysEditText.editableText.toString())) {
				customFrequencyDaysEditText.setText(DEFAULT_DAYS_VALUE)
			}
		}
	}

	constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

	fun getTimes() = customFrequencyTimesEditText.text.toString().toInt()
	fun setTimes(times: Int) {
		isUserAction = false
		customFrequencyTimesEditText.setText(times.toString())
		isUserAction = true
	}

	fun getDays() = customFrequencyDaysEditText.text.toString().toInt()
	fun setDays(days: Int) {
		isUserAction = false
		customFrequencyDaysEditText.setText(days.toString())
		isUserAction = true
	}

	fun setError(errorMessage: String){
		customFrequencyTimesEditText.error = errorMessage
		customFrequencyDaysEditText.error = errorMessage
	}

	private fun isNotValid(text: String): Boolean = text.isBlank() && text.toInt() <= 0

	companion object{
		private const val DEFAULT_TIMES_VALUE = 1.toString()
		private const val DEFAULT_DAYS_VALUE = 7.toString()
	}
}
