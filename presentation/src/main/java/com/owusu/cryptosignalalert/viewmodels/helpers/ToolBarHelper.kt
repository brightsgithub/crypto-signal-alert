package com.owusu.cryptosignalalert.viewmodels.helpers

import androidx.annotation.StringRes
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.models.SharedViewState
import com.owusu.cryptosignalalert.navigation.*
import com.owusu.cryptosignalalert.resource.AppStringProvider
import kotlinx.coroutines.flow.MutableStateFlow

class ToolBarHelper(private val appStringProvider: AppStringProvider) {

    private lateinit var _sharedViewState: MutableStateFlow<SharedViewState>

    fun initToolBarHelper(state: MutableStateFlow<SharedViewState>) {
        _sharedViewState = state
    }

    fun handleToolBarIconVisibility(route: String?) {

        if (route == null) return

        when (route) {
            NavigationItem.Home.route -> {
                showAllActionItems()
                hideUpBtnIcon()
                setTitle(R.string.app_name)
            }
            NavigationItem.PriceTargets.route -> {
                showAllActionItems()
                hideUpBtnIcon()
                setTitle(R.string.app_name)
            }
            NavigationItem.Purchase.route -> {
                showAllActionItems()
                hideUpBtnIcon()
                setTitle(R.string.app_name)
            }
            TargetEntryScreens.PriceTargetEntry.route -> {
                showOnlySettings()
                showUpBtnIcon()
                setTitle(R.string.price_target_entry_scr_title)
            }
            CoinSearchScreens.CoinSearch.route -> {
                showAllActionItems()
                showUpBtnIcon()
                setTitle(R.string.search_scr_title)
            }
            SettingsScreens.Settings.route -> {
                hideAllActionItems()
                showUpBtnIcon()
                setTitle(R.string.settings_scr_title)
            }
            WebViewScreens.WebView.route -> {
                hideAllActionItems()
                showUpBtnIcon()
                setTitle(R.string.privacy_policy_scr_title)
            }
        }
    }

    private fun hideSettingsIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSettingsIcon = false
            )
        )
    }

    private fun showSettingsIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSettingsIcon = true
            )
        )
    }

    private fun showSearchIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSearchIcon = true
            )
        )
    }

    private fun hideSearchIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSearchIcon = false
            )
        )
    }

    private fun showUpBtnIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowUpButtonIcon = true
            )
        )
    }

    private fun hideUpBtnIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowUpButtonIcon = false
            )
        )
    }

    private fun setTitle(@StringRes resId: Int) {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                title = appStringProvider.getString(resId)
            )
        )
    }

    private fun showAllActionItems() {
        showSearchIcon()
        showSettingsIcon()
    }

    private fun hideAllActionItems() {
        hideSearchIcon()
        hideSettingsIcon()
    }

    private fun showOnlySettings() {
        showSettingsIcon()
        hideSearchIcon()
    }
}