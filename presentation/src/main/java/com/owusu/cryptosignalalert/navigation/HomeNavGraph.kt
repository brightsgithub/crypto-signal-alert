package com.owusu.cryptosignalalert.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.*
import org.koin.androidx.compose.getViewModel

// Since bottom bar uses its own NavHost, we have to pass it a new NavHostController
// https://developer.android.com/jetpack/compose/navigation#create-navhost
@Composable
fun HomeNavGraph(
    navHostController: NavHostController,
    onSearchBarClick: () -> Unit,
    onShowSnackBar: (msg: String, actionLabel: String, shouldShowIndefinite: Boolean, actionCallback: () -> Unit) -> Unit
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
            CoinsListScreen(sharedViewModel, navigateToPriceTargetEntryScreen = { selectedCoinUI ->
                sharedViewModel.selectedCoinUI = selectedCoinUI
                // i guess we can also nav to out coin entry which is not a bottom nav?
                navHostController.navigate(route = Graphs.TARGETS_ENTRY_GRAPH)
            }, onShowSnackBar = onShowSnackBar)
        }
        composable(NavigationItem.PriceTargets.route) {
            PriceTargetsScreen(onSearchBarClick = onSearchBarClick, onShowSnackBar = onShowSnackBar)
        }

        composable(NavigationItem.Purchase.route) {
            PurchaseScreen()
        }



        // nested graph
        targetsEntryGraph(navHostController, sharedViewModel, onShowSnackBar = onShowSnackBar)
        coinSearchNavGraph(navHostController, sharedViewModel, onShowSnackBar = onShowSnackBar)
        settingsNavGraph(navHostController, sharedViewModel, onNavigateToWebView = { url ->
            sharedViewModel.webViewUrl = url
            navHostController.navigate(route = Graphs.WEB_VIEW_NAV_GRAPH)
        })
        webViewNavGraph(navHostController, sharedViewModel)
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
