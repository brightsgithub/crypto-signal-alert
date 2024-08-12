package com.owusu.cryptosignalalert.viewmodels.udf.pricetargetentry

import com.owusu.cryptosignalalert.models.CoinDetailUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfUiState

data class PriceTargetEntryViewState(
    val coinDetailUI: CoinDetailUI? = null,
    val isLoading: Boolean = false,
    val priceTargetsMessage: PriceTargetsMessage = PriceTargetsMessage()
): UdfUiState

data class PriceTargetsMessage(
    val shouldShowMessage: Boolean = false,
    val isError: Boolean = false,
    val message: String = "",
    val ctaText: String = ""
)
