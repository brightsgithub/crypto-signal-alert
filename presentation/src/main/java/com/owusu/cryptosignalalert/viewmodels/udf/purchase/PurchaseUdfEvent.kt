package com.owusu.cryptosignalalert.viewmodels.udf.purchase

import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.viewmodels.udf.UdfEvent

sealed class PurchaseUdfEvent:UdfEvent {
    data class OnPurchaseClicked(val screenProxy: ScreenProxy, val sku: String): PurchaseUdfEvent()
    object LoadSkuDetails: PurchaseUdfEvent()
}