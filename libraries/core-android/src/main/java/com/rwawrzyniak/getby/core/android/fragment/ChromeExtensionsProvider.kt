package com.rwawrzyniak.getby.core.android.fragment

import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView

interface ChromeExtensionsProvider {
    fun getAppActionBar(): ActionBar
    fun getBottomNavigation(): BottomNavigationView
}
