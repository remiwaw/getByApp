package com.rwawrzyniak.getby.app

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rwawrzyniak.getby.R
import com.rwawrzyniak.getby.core.ChromeExtensionsProvider
import com.rwawrzyniak.getby.core.GlobalEvent
import com.rwawrzyniak.getby.core.ext.observeBroadcasts
import com.rwawrzyniak.getby.dagger.injector
import com.rwawrzyniak.getby.databinding.ActivityMainBinding
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onStop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ChromeExtensionsProvider {
    private val globalEventSubject by lazy { injector.getGlobalEventSubject() }

    override fun getAppActionBar(): ActionBar {
        return requireNotNull(supportActionBar)
    }

    override fun getBottomNavigation(): BottomNavigationView {
        return requireNotNull(bottom_navigation_view)
    }

    override fun onStart() {
        this.observeBroadcasts(IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        })
            .disposeBy(onStop)
            .subscribe{
                globalEventSubject.onNext(GlobalEvent.DateChanged)
            }

        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
        setSupportActionBar(mainActionBar)
    }
}
