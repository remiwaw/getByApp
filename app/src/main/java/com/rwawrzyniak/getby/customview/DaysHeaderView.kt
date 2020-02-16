package com.rwawrzyniak.getby.customview

import addDays
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import asCalenderddMMyyy
import com.rwawrzyniak.getby.R
import kotlinx.android.synthetic.main.day_header_element.view.*
import kotlinx.android.synthetic.main.days_list.view.*
import nextDay
import toDayHeaderDto

class DaysHeaderView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.days_list, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DaysHeaderView)

        val firstDayAsString = attributes.getString(R.styleable.DaysHeaderView_firstDayAsString)

        val firstDayAsCalandar = firstDayAsString.asCalenderddMMyyy

        listOf(
            firstDayAsCalandar,
            firstDayAsCalandar.nextDay,
            firstDayAsCalandar.addDays(2),
            firstDayAsCalandar.addDays(3)
        )
            .map { it.toDayHeaderDto }
            .forEach {
                label_a.dayNameView.text = it.shortName
                label_a.dayNumberView.text = it.number.toString()
            }

        attributes.recycle()
    }
}