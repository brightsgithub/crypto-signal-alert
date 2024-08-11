package com.owusu.cryptosignalalert.viewmodels.udf.home

import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfEvent

sealed class HomeUdfEvent: UdfEvent {
    data class OnCoinRowClicked(val selectedCoinUI: CoinUI): HomeUdfEvent()
    object OnSearchBarClicked: HomeUdfEvent()
    object OnSettingsClicked: HomeUdfEvent()
    data class ShowSnackBar(
        val msg: String,
        val actionLabel: String,
        val actionCallback: () -> Unit,
        val shouldShowIndefinite: Boolean
    ): HomeUdfEvent()
    object EventNothing: HomeUdfEvent()
}