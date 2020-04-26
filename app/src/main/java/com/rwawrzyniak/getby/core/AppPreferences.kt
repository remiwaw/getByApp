package com.rwawrzyniak.getby.core

import javax.inject.Inject

class AppPreferences @Inject internal constructor(private val preferencesHelper: PreferencesHelper){
	fun setHideArchivedHabits(isHideArchived: Boolean){
		preferencesHelper.putBoolean(HIDE_ARCHIVED_HABITS, isHideArchived)
	}

	fun getHideArchivedHabits(): Boolean = preferencesHelper.getBoolean(HIDE_ARCHIVED_HABITS, true)

	companion object{
		private const val HIDE_ARCHIVED_HABITS = "hideArchivedHabits"
	}
}
