package com.rwawrzyniak.getby.core.android.preferences

import android.content.SharedPreferences

class PreferencesHelper internal constructor(private val preferences: SharedPreferences) {
	fun putString( key: String,  value: String?) {
		preferences.edit().putString(key, value).apply()
	}

	fun getString( key: String): String {
		return preferences.getString(key, "")
	}

	fun putBoolean( key: String,  value: Boolean) {
		preferences.edit().putBoolean(key, value).apply()
	}

	fun getBoolean( key: String, defaultValue: Boolean = false): Boolean {
		return preferences.getBoolean(key, defaultValue)
	}

	fun putInt( key: String, value: Boolean) {
		preferences.edit().putBoolean(key, value).apply()
	}

	fun getInt( key: String): Int {
		return preferences.getInt(key, -1)
	}

	fun clear() {
		preferences.edit().clear().apply()
	}
}
