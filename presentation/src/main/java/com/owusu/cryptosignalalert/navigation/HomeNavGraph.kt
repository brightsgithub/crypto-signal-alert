package com.owusu.cryptosignalalert.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.CoinsListScreen
import com.owusu.cryptosignalalert.views.screens.PriceTargetEntryScreen
import com.owusu.cryptosignalalert.views.screens.PriceTargetsScreen
import org.koin.androidx.compose.getViewModel

// Since bottom bar uses its own NavHost, we have to pass it a new NavHostController
// https://developer.android.com/jetpack/compose/navigation#create-navhost
@Composable
fun HomeNavGraph(navHostController: NavHostController) {

    val sharedViewModel = getViewModel<SharedViewModel>()

    NavHost(
        navController = navHostController,
        route = Graphs.HOME_NAV_GRAPH,
        startDestination = NavigationItem.Home.route
    ) {

        composable(NavigationItem.Home.route) {
            CoinsListScreen(sharedViewModel, navigateToPriceTargetEntryScreen = { selectedCoinUI ->
                sharedViewModel.selectedCoinUI = selectedCoinUI
                // i guess we can also nav to out coin entry which is not a bottom nav?
                navHostController.navigate(route = Graphs.TARGETS_ENTRY_GRAPH)
            })
        }
        composable(NavigationItem.PriceTargets.route) {
            PriceTargetsScreen(sharedViewModel)
        }

        // nested graph
        targetsEntryGraph(navHostController, sharedViewModel)
    }
}

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home_screen", R.drawable.ic_baseline_home_24, "Home")
    object PriceTargets : NavigationItem("price_targets_screen", R.drawable.ic_baseline_notifications_active_24, "Targets")
}
