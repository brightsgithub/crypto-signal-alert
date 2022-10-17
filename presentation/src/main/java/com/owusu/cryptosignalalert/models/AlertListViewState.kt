package com.owusu.cryptosignalalert.models

data class AlertListViewState(
    val isLoading: Boolean = false,
    val priceTargets: List<PriceTargetUI> = listOf()
)
