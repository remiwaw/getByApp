package com.rwawrzyniak.getby.core

import javax.inject.Inject

class AppPreferences @Inject internal constructor(private val preferencesHelper: PreferencesHelper){
	fun setShowArchivedHabits(isShowArchived: Boolean){
		preferencesHelper.putBoolean(SHOW_ARCHIVED_HABITS_FLAG, isShowArchived)
	}

	fun getShowArchivedHabits(): Boolean = preferencesHelper.getBoolean(SHOW_ARCHIVED_HABITS_FLAG)

	companion object{
		private const val SHOW_ARCHIVED_HABITS_FLAG = "showArchivedHabits"
	}
}
