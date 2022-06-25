package com.owusu.cryptosignalalert.models

sealed class AlertListViewState {
    data class AlertListDataSuccess(val alertItemUI: AlertItemUI)
    object AlertListDataFailure
    object ShowLoadingState
    object StopLoadingState
}
