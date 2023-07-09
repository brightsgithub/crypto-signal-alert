package com.owusu.cryptosignalalert.models

data class PriceTargetEntryViewState(
    val coinDetailUI: CoinDetailUI? = null,
    val isLoading: Boolean = false,
    val priceTargetsMessage: PriceTargetsMessage = PriceTargetsMessage()
)

data class PriceTargetsMessage(
    val shouldShowMessage: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val ctaText: String = ""
)
