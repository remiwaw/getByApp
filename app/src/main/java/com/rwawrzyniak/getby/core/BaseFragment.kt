package com.rwawrzyniak.getby.core

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.rwawrzyniak.getby.R

abstract class BaseFragment : Fragment() {

    val actionBar by lazy { (activity as ChromeExtensionsProvider).getAppActionBar() }
    val bottomNavigationView by lazy {
        (activity as ChromeExtensionsProvider).getBottomNavigation()
    }

    open fun getChromeConfig(): ChromeConfiguration = ChromeConfiguration(
        showActionBarAddButton = true
    )

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureChrome(getChromeConfig())
    }
    // endregion

    // region Private Methods
    private fun configureChrome(config: ChromeConfiguration) {
        setupActionBar(config)
        updateHomeAsUpButtonState(config.showActionBarUpButton)
        setupBotoomNavigationBar(config)
    }

    private fun setupActionBar(config: ChromeConfiguration) {
        with(actionBar) {
            if (config.showActionBar) {
                show()
                title = config.actionBarTitle
            } else {
                hide()
            }
        }
    }

    private fun updateHomeAsUpButtonState(screenStateShowingUpButton: Boolean) {
        actionBar.setDisplayHomeAsUpEnabled(screenStateShowingUpButton) // TODO do i need it?
    }

    private fun setupBotoomNavigationBar(config: ChromeConfiguration) {
        if (config.showBottomNavigationBar) {
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.menu_top_add)
        item.isVisible = true
    }
}
