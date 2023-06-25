package com.owusu.cryptosignalalert.models

data class AlertListViewState(
    val isLoading: Boolean = false,
    val priceTargets: List<PriceTargetUI> = listOf(),
    val numberOfTargetsMet: Int = 0,
    val totalNumberOfTargets: Int = 0,
    val remainingSyncPercentageToBeUpdated: Float = 0f,
    val shouldShowSyncState: Boolean = false
)
