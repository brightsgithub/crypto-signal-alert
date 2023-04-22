package com.owusu.cryptosignalalert.models

sealed class PriceEntryScreenEvents {
    object SavePriceTargetSuccess: PriceEntryScreenEvents()
    object SavePriceTargetFailure: PriceEntryScreenEvents()
    object Nothing: PriceEntryScreenEvents()
}
