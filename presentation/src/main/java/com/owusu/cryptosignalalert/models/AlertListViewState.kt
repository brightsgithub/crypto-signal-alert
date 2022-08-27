package com.owusu.cryptosignalalert.models

sealed class AlertListViewState {
    data class AlertListDataSuccess(val priceTargets: List<PriceTargetUI>): AlertListViewState()
    object AlertListDataFailure: AlertListViewState()
    object ShowLoadingState: AlertListViewState()
    object HideLoadingState: AlertListViewState()
}
