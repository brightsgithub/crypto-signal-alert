package com.owusu.cryptosignalalert.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.CoinSearchScreen
import com.owusu.cryptosignalalert.views.screens.SettingsScreen

fun NavGraphBuilder.settingsNavGraph(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    navigation(
        route = Graphs.SETTINGS_NAV_GRAPH,
        startDestination = SettingsScreens.Settings.route,
        builder = {
            composable(route = SettingsScreens.Settings.route) {
                SettingsScreen()
            }
        }
    )
}

sealed class SettingsScreens(val route: String) {
    object Settings : SettingsScreens(route = "Settings")
    object SomeOtherSettingsScreen : CoinSearchScreens(route = "SomeOtherSettingsScreen")
}