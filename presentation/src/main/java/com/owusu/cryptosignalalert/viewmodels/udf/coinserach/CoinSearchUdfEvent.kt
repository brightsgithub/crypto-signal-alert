package com.owusu.cryptosignalalert.viewmodels.udf.coinserach

import com.owusu.cryptosignalalert.models.CoinIdUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfEvent

sealed class CoinSearchUdfEvent:UdfEvent {

    data class OnSearchTextChanged(val changedSearchText: String):CoinSearchUdfEvent()
    data class OnSearchItemSelected(val coinIdUI: CoinIdUI):CoinSearchUdfEvent()
    object OnClearClicked:CoinSearchUdfEvent()
    object HideSnackBar:CoinSearchUdfEvent()
}