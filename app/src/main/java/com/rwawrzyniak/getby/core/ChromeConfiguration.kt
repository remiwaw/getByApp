package com.rwawrzyniak.getby.core

data class ChromeConfiguration(
    val showActionBar: Boolean = false,
    val showActionBarUpButton: Boolean = true,
    val showActionBarAddButton: Boolean = false,
    val actionBarTitle: String? = null,
    val showBottomNavigationBar: Boolean = false
)
