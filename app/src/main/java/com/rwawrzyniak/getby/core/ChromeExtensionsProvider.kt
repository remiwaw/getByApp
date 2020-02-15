package com.rwawrzyniak.getby.core

import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView

interface ChromeExtensionsProvider {
    fun getAppActionBar(): ActionBar
    fun getBottomNavigation(): BottomNavigationView
}
