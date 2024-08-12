package com.owusu.cryptosignalalert.viewmodels.helpers

import androidx.annotation.StringRes
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.viewmodels.udf.home.SharedViewState
import com.owusu.cryptosignalalert.navigation.*
import com.owusu.cryptosignalalert.resource.AppStringProvider
import kotlinx.coroutines.flow.StateFlow

class ToolBarHelper(private val appStringProvider: AppStringProvider) {

    private lateinit var uiState: StateFlow<SharedViewState>
    private lateinit var setTheUiState: (uiState: SharedViewState) -> Unit

    fun initToolBarHelper(uiState: StateFlow<SharedViewState>, setTheUiState: (uiState: SharedViewState) -> Unit) {
        this.uiState = uiState
        this.setTheUiState = setTheUiState
    }

    fun handleToolBarVisibility(route: String?) {

        if (route == null) return

        when (route) {
            NavigationItem.Home.route -> {
                showAllActionItems()
                hideUpBtnIcon()
                showToolBar()
                setTitle(R.string.app_name)
            }
            NavigationItem.PriceTargets.route -> {
                showAllActionItems()
                hideUpBtnIcon()
                showToolBar()
                setTitle(R.string.app_name)
            }
            NavigationItem.Purchase.route -> {
                showAllActionItems()
                hideUpBtnIcon()
                showToolBar()
                setTitle(R.string.app_name)
            }
            TargetEntryScreens.PriceTargetEntry.route -> {
                showOnlySettings()
                showUpBtnIcon()
                showToolBar()
                setTitle(R.string.price_target_entry_scr_title)
            }
            CoinSearchScreens.CoinSearch.route -> {
                showAllActionItems()
                showUpBtnIcon()
                hideToolBar()
                setTitle(R.string.search_scr_title)
            }
            SettingsScreens.Settings.route -> {
                hideAllActionItems()
                showUpBtnIcon()
                showToolBar()
                setTitle(R.string.settings_scr_title)
            }
            WebViewScreens.WebView.route -> {
                hideAllActionItems()
                showUpBtnIcon()
                showToolBar()
                setTitle(R.string.privacy_policy_scr_title)
            }
        }
    }

    private fun hideSettingsIcon() {

        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowSettingsIcon = false
            )
        )
        setTheUiState(newUiState)
//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowSettingsIcon = false
//            )
//        )
    }

    private fun showSettingsIcon() {

        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowSettingsIcon = true
            )
        )
        setTheUiState(newUiState)
//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowSettingsIcon = true
//            )
//        )
    }

    private fun showSearchIcon() {

        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowSearchIcon = true
            )
        )
        setTheUiState(newUiState)

//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowSearchIcon = true
//            )
//        )
    }

    private fun hideSearchIcon() {

        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowSearchIcon = false
            )
        )
        setTheUiState(newUiState)
//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowSearchIcon = false
//            )
//        )
    }

    private fun showUpBtnIcon() {

        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowUpButtonIcon = true
            )
        )
        setTheUiState(newUiState)
//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowUpButtonIcon = true
//            )
//        )
    }

    private fun hideUpBtnIcon() {

        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowUpButtonIcon = false
            )
        )
        setTheUiState(newUiState)

//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowUpButtonIcon = false
//            )
//        )
    }

    private fun showToolBar() {


        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowToolTar = true
            )
        )
        setTheUiState(newUiState)

//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowToolTar = true
//            )
//        )
    }

    private fun hideToolBar() {

        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                shouldShowToolTar = false
            )
        )
        setTheUiState(newUiState)
//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                shouldShowToolTar = false
//            )
//        )
    }
    private fun setTitle(@StringRes resId: Int) {


        val newUiState = uiState.value.copy(
            actionButtonState = uiState.value.actionButtonState.copy(
                title = appStringProvider.getString(resId)
            )
        )
        setTheUiState(newUiState)
//        _sharedViewState.value = _sharedViewState.value.copy(
//            actionButtonState = _sharedViewState.value.actionButtonState.copy(
//                title = appStringProvider.getString(resId)
//            )
//        )
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