package com.rwawrzyniak.getby.core.ext

import ca.antonious.materialdaypicker.MaterialDayPicker
import java.time.DayOfWeek

fun convertWeekDaysToMaterial (weekDays: List<DayOfWeek>): List<MaterialDayPicker.Weekday> = weekDays.map { it.toMaterial() }
fun convertWeekDaysToStandard (weekDays: List<MaterialDayPicker.Weekday>): List<DayOfWeek> = weekDays.map { it.toStandard() }

fun DayOfWeek.toMaterial(): MaterialDayPicker.Weekday = when(this){
	DayOfWeek.SUNDAY -> MaterialDayPicker.Weekday.SUNDAY
	DayOfWeek.MONDAY -> MaterialDayPicker.Weekday.MONDAY
	DayOfWeek.TUESDAY -> MaterialDayPicker.Weekday.TUESDAY
	DayOfWeek.WEDNESDAY -> MaterialDayPicker.Weekday.WEDNESDAY
	DayOfWeek.THURSDAY -> MaterialDayPicker.Weekday.THURSDAY
	DayOfWeek.FRIDAY -> MaterialDayPicker.Weekday.FRIDAY
	DayOfWeek.SATURDAY -> MaterialDayPicker.Weekday.SATURDAY
}


fun MaterialDayPicker.Weekday.toStandard(): DayOfWeek = when(this){
	MaterialDayPicker.Weekday.SUNDAY -> DayOfWeek.SUNDAY
	MaterialDayPicker.Weekday.MONDAY -> DayOfWeek.MONDAY
	MaterialDayPicker.Weekday.TUESDAY -> DayOfWeek.TUESDAY
	MaterialDayPicker.Weekday.WEDNESDAY -> DayOfWeek.WEDNESDAY
	MaterialDayPicker.Weekday.THURSDAY -> DayOfWeek.THURSDAY
	MaterialDayPicker.Weekday.FRIDAY -> DayOfWeek.FRIDAY
	MaterialDayPicker.Weekday.SATURDAY -> DayOfWeek.SATURDAY
}

