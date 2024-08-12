package com.owusu.cryptosignalalert.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.PriceTargetEntryScreen

fun NavGraphBuilder.targetsEntryGraph(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    navigation(
        route = Graphs.TARGETS_ENTRY_GRAPH,
        startDestination = TargetEntryScreens.PriceTargetEntry.route,
        builder = {
            composable(route = TargetEntryScreens.PriceTargetEntry.route) {
                PriceTargetEntryScreen(sharedViewModel = sharedViewModel)
            }
        }
    )
}

sealed class TargetEntryScreens(val route: String) {
    object PriceTargetEntry : TargetEntryScreens(route = "PriceTargetEntry")
    object SomeOtherEntryScreen : TargetEntryScreens(route = "SomeOtherEntryScreen")
}