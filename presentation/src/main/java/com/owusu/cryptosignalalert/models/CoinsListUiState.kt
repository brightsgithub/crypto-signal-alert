package com.owusu.cryptosignalalert.models

data class CoinsListUiState(
    val coinsListUiStateMessage: CoinsListUiStateMessage = CoinsListUiStateMessage()
)

data class CoinsListUiStateMessage(
    val shouldShowMessage: Boolean = false,
    val message: String = "",
    val ctaText: String = ""
)
