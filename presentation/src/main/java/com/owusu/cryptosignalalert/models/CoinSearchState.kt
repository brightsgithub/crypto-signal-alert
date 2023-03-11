package com.owusu.cryptosignalalert.models

data class CoinSearchState(val searchStr: String = "",
                           val coinIds: List<CoinIdUI> = arrayListOf(),
                           val showProgressBar: Boolean = false,
                           val resultSize:Int = 0
) {
    companion object {
        val Empty = CoinSearchState()
    }
}

sealed class CoinSearchStateEvents {
    object NOTHING: CoinSearchStateEvents()
    data class NavigateToPriceTargetEntryScreen(val coinUI: CoinUI): CoinSearchStateEvents()
}
