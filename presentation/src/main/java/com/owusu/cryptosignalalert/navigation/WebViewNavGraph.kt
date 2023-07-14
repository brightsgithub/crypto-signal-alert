package com.owusu.cryptosignalalert.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.WebViewScreen

fun NavGraphBuilder.webViewNavGraph(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    navigation(
        route = Graphs.WEB_VIEW_NAV_GRAPH,
        startDestination = WebViewScreens.WebView.route,
        builder = {
            composable(route = WebViewScreens.WebView.route) {
                WebViewScreen(sharedViewModel.webViewUrl)
            }
        }
    )
}

sealed class WebViewScreens(val route: String) {
    object WebView : WebViewScreens(route = "privacy_policy")
    object SomeOtherWebViewScreens : WebViewScreens(route = "SomeOtherWebViewScreens")
}