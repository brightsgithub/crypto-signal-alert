package com.owusu.cryptosignalalert.models

data class CoinSearchState(val searchStr: String = "",
                           val coinIds: List<CoinIdUI> = arrayListOf(),
                           val showProgressBar: Boolean = false) {
    companion object {
        val Empty = CoinSearchState()
    }
}
