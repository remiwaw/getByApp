package com.rwawrzyniak.getby.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.rwawrzyniak.getby.R
import kotlinx.android.synthetic.main.custom_frequency_element.view.*

class CustomFrequencyView : ConstraintLayout {

    init {
        inflate(context, R.layout.custom_frequency_element, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        parseAttributes(attrs)
    }

	fun getTimes() = customFrequencyTimesEditText.text.toString().toLong()

	fun getDays() = customFrequencyDaysEditText.text.toString().toLong()

	private fun parseAttributes(attrs: AttributeSet?) {
        val typedAttrs =
            context.theme.obtainStyledAttributes(attrs, R.styleable.DayHeaderView, 0, 0)

        typedAttrs.recycle()
    }


}
