package com.owusu.cryptosignalalert.data.enpoints

abstract class EndPointsParent {

    abstract fun getHostName(): String

    fun getCoinsListWithMarketDataPath(): String {
        return "/api/v3/coins/markets"
    }
}