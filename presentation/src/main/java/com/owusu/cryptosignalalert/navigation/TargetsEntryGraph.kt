package com.owusu.cryptosignalalert.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.PriceTargetEntryScreen

fun NavGraphBuilder.targetsEntryGraph(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel,
    onShowSnackBar: (msg: String, actionLabel: String, shouldShowIndefinite: Boolean, actionCallback: () -> Unit) -> Unit
) {
    navigation(
        route = Graphs.TARGETS_ENTRY_GRAPH,
        startDestination = TargetEntryScreens.PriceTargetEntry.route,
        builder = {
            composable(route = TargetEntryScreens.PriceTargetEntry.route) {
                PriceTargetEntryScreen(sharedViewModel = sharedViewModel, navigateToTargetsList = {
                    navHostController.navigate(route = NavigationItem.PriceTargets.route) {

                        // remove the PriceTargetEntryScreen when moving to the next destination
                        // https://stackoverflow.com/questions/66845899/compose-navigation-remove-previous-composable-from-stack-before-navigating
                        popUpTo(route = Graphs.TARGETS_ENTRY_GRAPH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                    navigateToPurchaseScreen = {
                    navHostController.navigate(route = NavigationItem.Purchase.route)
                },
                    onShowSnackBar = onShowSnackBar)
            }
        }
    )
}

sealed class TargetEntryScreens(val route: String) {
    object PriceTargetEntry : TargetEntryScreens(route = "PriceTargetEntry")
    object SomeOtherEntryScreen : TargetEntryScreens(route = "SomeOtherEntryScreen")
}