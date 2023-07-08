package com.owusu.cryptosignalalert.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.owusu.cryptosignalalert.views.screens.HomeScreen

@Composable
fun RootNavigationGraph(navHostController: NavHostController, preselectedScreen: MutableState<String?>) {
    NavHost(
        navController = navHostController,
        route = Graphs.MAIN_ROOT_GRAPH,
        startDestination = Graphs.HOME_NAV_GRAPH,
        builder = {
            // list destinations/graphs

            // OnBoarding graph
            // SplashScreen graph
            composable(route = Graphs.HOME_NAV_GRAPH) {
                HomeScreen(preselectedScreen = preselectedScreen)
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
}