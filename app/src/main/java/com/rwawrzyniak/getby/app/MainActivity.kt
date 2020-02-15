package com.rwawrzyniak.getby.app

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.ChromeExtensionsProvider
import com.rwawrzyniak.getby.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ChromeExtensionsProvider {

	override fun getAppActionBar(): ActionBar {
		return requireNotNull(supportActionBar)
	}

	override fun getBottomNavigation(): BottomNavigationView {
		return requireNotNull(bottom_navigation_view)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
		setSupportActionBar(mainActionBar)
	}
}
