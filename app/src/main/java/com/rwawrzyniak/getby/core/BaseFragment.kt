package com.rwawrzyniak.getby.core

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rwawrzyniak.getby.R
import kotlinx.android.synthetic.main.activity_main.*

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

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		if (id == android.R.id.home) {
			goBack()
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	protected fun goBack() {
		nav_host.findNavController().popBackStack()
	}

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
		menu.findItem(R.id.top_add).isVisible = getChromeConfig().showActionBarAddButton
		menu.findItem(R.id.action_save).isVisible = getChromeConfig().showActionBarSaveButton
		menu.findItem(R.id.action_edit).isVisible = getChromeConfig().showActionBarEditButton
    }
}
