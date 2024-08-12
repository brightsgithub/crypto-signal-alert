package com.owusu.cryptosignalalert.viewmodels.udf.coinserach

import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfAction

sealed class CoinSearchUdfAction: UdfAction {
    object NOTHING: CoinSearchUdfAction()
    data class NavigateToPriceTargetEntryScreen(val coinUI: CoinUI): CoinSearchUdfAction()
}