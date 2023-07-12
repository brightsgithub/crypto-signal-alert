package com.owusu.cryptosignalalert.domain.models.states

data class PurchasedStateDomain(
    val isAppFree: Boolean = false,
    val isPriceTargetLimitPurchased: Boolean = false,
    val isAdsPurchased: Boolean = false
)
