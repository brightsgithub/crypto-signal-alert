package com.owusu.cryptosignalalert.viewmodels.udf.pricetargetlist

import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfEvent

sealed class PriceTargetListUdfEvent: UdfEvent {
    object Initialize: PriceTargetListUdfEvent()
    data class DeletePriceTarget(val priceTargetUI: PriceTargetUI): PriceTargetListUdfEvent()
}