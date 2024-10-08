package com.owusu.cryptosignalalert.viewmodels.udf.coinslist

import com.owusu.cryptosignalalert.viewmodels.udf.UdfUiState

data class CoinsListUiState(
    val coinsListUiStateMessage: CoinsListUiStateMessage = CoinsListUiStateMessage()
): UdfUiState

data class CoinsListUiStateMessage(
    val shouldShowMessage: Boolean = false,
    val message: String = "",
    val ctaText: String = ""
)
