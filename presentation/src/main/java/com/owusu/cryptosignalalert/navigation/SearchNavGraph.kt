package com.owusu.cryptosignalalert.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.CoinSearchScreen

fun NavGraphBuilder.coinSearchNavGraph(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    navigation(
        route = Graphs.SEARCH_NAV_GRAPH,
        startDestination = CoinSearchScreens.CoinSearch.route,
        builder = {
            composable(route = CoinSearchScreens.CoinSearch.route) {
                CoinSearchScreen(sharedViewModel = sharedViewModel)
            }
        }
    )
}

sealed class CoinSearchScreens(val route: String) {
    object CoinSearch : CoinSearchScreens(route = "CoinSearch")
    object SomeOtherCoinSearchScreen : CoinSearchScreens(route = "SomeOtherSearchScreen")
}