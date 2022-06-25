package com.owusu.cryptosignalalert.models

sealed class AlertListViewState {
    data class AlertListDataSuccess(val alertListUIWrapper: AlertListUIWrapper): AlertListViewState()
    object AlertListDataFailure: AlertListViewState()
    object ShowLoadingState: AlertListViewState()
    object HideLoadingState: AlertListViewState()
}
