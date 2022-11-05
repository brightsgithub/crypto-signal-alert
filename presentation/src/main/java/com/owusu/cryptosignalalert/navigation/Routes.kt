package com.owusu.cryptosignalalert.navigation

sealed class Routes(val route: String) {
    object PriceTargetEntry : Routes(route = "price_targets_entry")
}
