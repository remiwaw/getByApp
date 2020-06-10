package com.rwawrzyniak.getby.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rwawrzyniak.getby.entities.Frequency
import com.rwawrzyniak.getby.entities.HabitDay
import com.rwawrzyniak.getby.entities.HourMinute
import com.rwawrzyniak.getby.entities.Reminder
import java.lang.reflect.Type
import java.time.DayOfWeek
import java.util.Date

// TODO if this becomes to slow, consider not using gson everywhere. Just write converter code.
class RoomMarshal {

	private val gson = Gson()

	// HabitDay list converer
	var founderListType: Type = object : TypeToken<ArrayList<HabitDay>>() {}.type

	@TypeConverter
	fun fromJsonToHabitDayList(json: String?): List<HabitDay>? = gson.fromJson(json, founderListType)

	@TypeConverter
	fun habitDayListToJson(habitDayList: List<HabitDay>?): String? = gson.toJson(habitDayList)

	// HabitDay converer
	@TypeConverter
	fun fromJsonToHabitDay(json: String?): HabitDay? = gson.fromJson(json, HabitDay::class.java)

	@TypeConverter
	fun habitDayToJson(habitDay: HabitDay?): String? = gson.toJson(habitDay)

	// Reminder converter
	@TypeConverter
	fun fromJsonToReminder(json: String?): Reminder? = gson.fromJson(json, Reminder::class.java)

	@TypeConverter
	fun reminderToJson(reminder: Reminder?): String? = gson.toJson(reminder)

	// Day of week converter
	@TypeConverter
	fun fromJsonToDayOfWeek(json: String?): DayOfWeek = gson.fromJson(json, DayOfWeek::class.java)

	@TypeConverter
	fun dayOfWeekToJson(dayOfWeek: DayOfWeek?): String = gson.toJson(dayOfWeek)

	// HourMinute converter
	@TypeConverter
	fun fromHourMinuteJson(json: String?): HourMinute = gson.fromJson(json, HourMinute::class.java)

	@TypeConverter
	fun hourMinuteToJson(hourMinute: HourMinute?): String = gson.toJson(hourMinute)

	// Frequency converter
	@TypeConverter
	fun fromFrequencyJson(json: String?): Frequency = gson.fromJson(json, Frequency::class.java)

	@TypeConverter
	fun frequencyToJson(frequency: Frequency?): String = gson.toJson(frequency)

	// Date converter
	@TypeConverter
	fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

	@TypeConverter
	fun dateToTimestamp(date: Date?): Long? = date?.time

	// List sting converters
	@TypeConverter
	fun fromString(value: String): List<String> {
		val listType: Type = object : TypeToken<List<String>>() {}.type
		return Gson().fromJson(value, listType)
	}

	@TypeConverter
	fun fromArrayList(list: List<String>): String {
		return gson.toJson(list)
	}
}
