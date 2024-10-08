package com.owusu.cryptosignalalert.viewmodels.udf.coinserach

import com.owusu.cryptosignalalert.models.CoinIdUI
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfUiState

data class CoinSearchState(val searchStr: String = "",
                           val coinIds: List<CoinIdUI> = arrayListOf(),
                           val showProgressBar: Boolean = false,
                           val resultSize:Int = 0,
                           val coinSearchStateMessage: CoinSearchStateMessage = CoinSearchStateMessage()
): UdfUiState {
    companion object {
        val Empty = CoinSearchState()
    }
}

data class CoinSearchStateMessage(
    val shouldShowMessage: Boolean = false,
    val message: String = "",
    val ctaText: String = ""
)

sealed class CoinSearchStateEvents {
    object NOTHING: CoinSearchStateEvents()
    data class NavigateToPriceTargetEntryScreen(val coinUI: CoinUI): CoinSearchStateEvents()
}
