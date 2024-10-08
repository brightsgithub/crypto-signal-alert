package com.owusu.cryptosignalalert.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.ads.AdRequest
import com.owusu.cryptosignalalert.views.screens.HomeScreen
import com.owusu.cryptosignalalert.views.theme.AppTheme

@Composable
fun RootNavigationGraph(
    navHostController: NavHostController,
    preselectedScreen: MutableState<String?>,
    adRequest: AdRequest
    ) {
    NavHost(
        navController = navHostController,
        route = Graphs.MAIN_ROOT_GRAPH,
        startDestination = Graphs.HOME_NAV_GRAPH,
        builder = {
            // list destinations/graphs

            // OnBoarding graph
            // SplashScreen graph
            composable(route = Graphs.HOME_NAV_GRAPH) {
               // AppTheme {
                    HomeScreen(preselectedScreen = preselectedScreen, adRequest = adRequest)
              //  }
            }
        }
    )
}

object Graphs {
    const val MAIN_ROOT_GRAPH = "MAIN_ROOT_GRAPH"
    const val HOME_NAV_GRAPH = "HOME_NAV_GRAPH"
    const val TARGETS_ENTRY_GRAPH = "TARGETS_ENTRY_GRAPH"
    const val SEARCH_NAV_GRAPH = "SEARCH_NAV_GRAPH"
    const val SETTINGS_NAV_GRAPH = "SETTINGS_NAV_GRAPH"
    const val WEB_VIEW_NAV_GRAPH = "WEB_VIEW_NAV_GRAPH"
}