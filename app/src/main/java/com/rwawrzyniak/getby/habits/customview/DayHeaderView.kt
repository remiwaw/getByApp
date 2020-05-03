package com.rwawrzyniak.getby.habits.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.rwawrzyniak.getby.R

class DayHeaderView : ConstraintLayout {

    init {
        inflate(context, R.layout.day_header_element, this)
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

    private fun parseAttributes(attrs: AttributeSet?) {
        val typedAttrs =
            context.theme.obtainStyledAttributes(attrs, R.styleable.DayHeaderView, 0, 0)

        typedAttrs.recycle()
    }


}
