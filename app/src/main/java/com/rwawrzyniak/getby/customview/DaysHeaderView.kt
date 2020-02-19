package com.rwawrzyniak.getby.customview

import addDays
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.rwawrzyniak.getby.R
import kotlinx.android.synthetic.main.day_header_element.view.*
import kotlinx.android.synthetic.main.days_list.view.*
import toDayHeaderDto
import java.util.Calendar

class DaysHeaderView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.days_list, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DaysHeaderView)

        //Uncomment for preview
        // val firstDayAsString = attributes.getString(R.styleable.DaysHeaderView_firstDayAsString)
        // val firstDayAsCalendar = firstDayAsString.asCalenderddMMyyy
        // initializeDaysHeader(firstDayAsCalendar)

        attributes.recycle()
    }

    fun initializeDaysHeader(firstDayAsCalendar: Calendar) {
        days_list_layout.children.forEachIndexed { index, item: View ->
            val currentDay = firstDayAsCalendar.addDays(index).toDayHeaderDto
            item.dayNameView.text = currentDay.shortName
            item.dayNumberView.text = currentDay.number.toString()
        }
    }
}