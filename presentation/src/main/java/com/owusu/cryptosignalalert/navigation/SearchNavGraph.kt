package com.owusu.cryptosignalalert.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.CoinSearchScreen

fun NavGraphBuilder.coinSearchNavGraph(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel,
    onShowSnackBar: (msg: String, actionLabel: String, shouldShowIndefinite: Boolean, actionCallback: () -> Unit) -> Unit
) {
    navigation(
        route = Graphs.SEARCH_NAV_GRAPH,
        startDestination = CoinSearchScreens.CoinSearch.route,
        builder = {
            composable(route = CoinSearchScreens.CoinSearch.route) {
                CoinSearchScreen(sharedViewModel = sharedViewModel, navigateToPriceTargetEntryScreen = { selectedCoinUI ->
                    sharedViewModel.selectedCoinUI = selectedCoinUI
                    navHostController.navigate(route = TargetEntryScreens.PriceTargetEntry.route) {
                        // remove the SearchScreen when moving to the next destination
                        // https://stackoverflow.com/questions/66845899/compose-navigation-remove-previous-composable-from-stack-before-navigating
                        popUpTo(route = Graphs.SEARCH_NAV_GRAPH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }, onShowSnackBar = onShowSnackBar)
            }
        }
    )
}

sealed class CoinSearchScreens(val route: String) {
    object CoinSearch : CoinSearchScreens(route = "CoinSearch")
    object SomeOtherCoinSearchScreen : CoinSearchScreens(route = "SomeOtherSearchScreen")
}