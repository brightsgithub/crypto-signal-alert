package com.owusu.cryptosignalalert.navigation

import com.owusu.cryptosignalalert.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home_screen", R.drawable.ic_baseline_home_24, "Home")
    object PriceTargets : NavigationItem("price_targets_screen", R.drawable.ic_baseline_notifications_active_24, "Targets")
}
