package com.rwawrzyniak.getby.date

import java.util.Calendar
import java.util.Date

fun Date.toCalendar(): Calendar = Calendar.getInstance().also {
    it.timeInMillis = this.time
}
