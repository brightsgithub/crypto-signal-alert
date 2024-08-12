package com.owusu.cryptosignalalert.viewmodels.udf.pricetargetentry

import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfEvent

sealed class PriceTargetEntryUdfEvent: UdfEvent {
    data class GetCoinDetails(val coinUI: CoinUI): PriceTargetEntryUdfEvent()
    data class SaveNewPriceTarget(val coinUI: CoinUI, val userPriceTarget: String): PriceTargetEntryUdfEvent()
}
