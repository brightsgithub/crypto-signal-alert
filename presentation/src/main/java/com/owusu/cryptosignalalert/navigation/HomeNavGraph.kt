package com.owusu.cryptosignalalert.navigation

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.home.HomeUdfAction
import com.owusu.cryptosignalalert.views.screens.*
import org.koin.androidx.compose.getViewModel

// Since bottom bar uses its own NavHost, we have to pass it a new NavHostController
// https://developer.android.com/jetpack/compose/navigation#create-navhost
@Composable
fun HomeNavGraph(
    navHostController: NavHostController
) {

    val sharedViewModel = getViewModel<SharedViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    initBilling(sharedViewModel, lifecycleOwner)

    NavHost(
        navController = navHostController,
        route = Graphs.HOME_NAV_GRAPH,
        startDestination = NavigationItem.Home.route
    ) {

        composable(NavigationItem.Home.route) {
            CoinsListScreen(sharedViewModel)
        }
        composable(NavigationItem.PriceTargets.route) {
            PriceTargetsScreen(sharedViewModel)
        }

        composable(NavigationItem.Purchase.route) {
            PurchaseScreen()
        }

        // nested graph
        targetsEntryGraph(navHostController, sharedViewModel)
        coinSearchNavGraph(navHostController, sharedViewModel)
        settingsNavGraph(navHostController, sharedViewModel)
        webViewNavGraph(navHostController, sharedViewModel)
    }


    // Not collecting as state, since I always want to listen in to the the flow, not just
    // state changes, as the user could click the same action multiple times
    LaunchedEffect(Unit) {
        sharedViewModel.action.collect{ action ->
            when (action) {
                is HomeUdfAction.NavigateToSearch -> {
                    navHostController.navigate(route = CoinSearchScreens.CoinSearch.route) {
                        // Navigate to the "search” destination only if we’re not already on
                        // the "search" destination, avoiding multiple copies on the top of the
                        // back stack
                        // YOU MUST USE ROUTE NOT GRAPH!
                        launchSingleTop = true
                    }
                }
                is HomeUdfAction.NavigateToSettings -> {
                    navHostController.navigate(route = SettingsScreens.Settings.route) {
                        // Navigate to the "settings” destination only if we’re not already on
                        // the "settings" destination, avoiding multiple copies on the top of the
                        // back stack
                        // YOU MUST USE ROUTE NOT GRAPH!
                        launchSingleTop = true
                    }
                }
                is HomeUdfAction.NavigateToPriceTargetEntry -> {
                    // i guess we can also nav to out coin entry which is not a bottom nav?
                    navHostController.navigate(route = Graphs.TARGETS_ENTRY_GRAPH)
                }
                is HomeUdfAction.NavigateToPriceTargets -> {
                    navHostController.navigate(route = NavigationItem.PriceTargets.route) {

                        // remove the PriceTargetEntryScreen when moving to the next destination
                        // https://stackoverflow.com/questions/66845899/compose-navigation-remove-previous-composable-from-stack-before-navigating
                        popUpTo(route = Graphs.TARGETS_ENTRY_GRAPH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                is HomeUdfAction.NavigateToPurchase -> {
                    navHostController.navigate(route = NavigationItem.Purchase.route)
                }
                is HomeUdfAction.NavigateToPriceTargetEntryFromSearch -> {
                    navHostController.navigate(route = TargetEntryScreens.PriceTargetEntry.route) {
                        // remove the SearchScreen when moving to the next destination
                        // https://stackoverflow.com/questions/66845899/compose-navigation-remove-previous-composable-from-stack-before-navigating
                        popUpTo(route = Graphs.SEARCH_NAV_GRAPH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                is HomeUdfAction.NavigateToWebView -> {
                    navHostController.navigate(route = Graphs.WEB_VIEW_NAV_GRAPH)
                }
                else -> { }
            }
        }
    }

}

@Composable
private fun initBilling(
    sharedViewModel: SharedViewModel,
    lifecycleOwner: LifecycleOwner
) {
    // Only executed once, not on every recomposition
    // https://www.youtube.com/watch?v=gxWcfz3V2QE&t=298s
    DisposableEffect(key1 = true) {

        val observer = LifecycleEventObserver { _, event ->
            Log.v("The_current_life", event.toString())

            if (event == Lifecycle.Event.ON_CREATE) {
                // only observe when in onCreate() has been called
                sharedViewModel.initApp()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        // compose is done so the below is called
        onDispose {
            Log.v("The_current_life", "onDispose() called")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home_screen", R.drawable.ic_baseline_home_24, "Home")
    object PriceTargets : NavigationItem("price_targets_screen", R.drawable.ic_baseline_notifications_active_24, "Targets")
    object Purchase : NavigationItem("purchase_screen", R.drawable.ic_baseline_shop_24, "Shop")
}
