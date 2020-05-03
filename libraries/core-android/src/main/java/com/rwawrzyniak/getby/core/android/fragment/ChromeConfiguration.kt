package com.rwawrzyniak.getby.core.android.fragment

data class ChromeConfiguration(
    val showActionBar: Boolean = false,
	val actionBarTitle: String? = null,
	val showActionBarUpButton: Boolean = true,
    val showActionBarAddButton: Boolean = false,
    val showActionBarSaveButton: Boolean = false,
    val showActionBarEditButton: Boolean = false,
    val showBottomNavigationBar: Boolean = false
)
