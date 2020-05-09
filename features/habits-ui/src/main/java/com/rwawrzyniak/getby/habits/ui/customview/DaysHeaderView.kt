package com.rwawrzyniak.getby.habits.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.rwawrzyniak.getby.habits.R
import com.rwawrzyniak.getby.habits.ui.persistance.DayHeaderDto
import kotlinx.android.synthetic.main.day_header_element.view.*
import kotlinx.android.synthetic.main.days_list.view.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DaysHeaderView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.days_list, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DaysHeaderView)
        attributes.recycle()
    }

    fun initializeDaysHeader(firstDayAsCalendar: LocalDate) {
        days_list_layout.children.toList().reversed().forEachIndexed { index, item: View ->
            val currentDay = firstDayAsCalendar.minusDays(index.toLong()).toDayHeaderDto
            item.dayNameView.text = currentDay.shortName
            item.dayNumberView.text = currentDay.number.toString()
        }
    }

	private val LocalDate.toDayHeaderDto: DayHeaderDto
		get() = DayHeaderDto(
			this.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()),
			this.dayOfMonth
		)
}
