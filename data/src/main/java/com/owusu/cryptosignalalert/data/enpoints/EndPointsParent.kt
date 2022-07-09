package com.owusu.cryptosignalalert.data.enpoints

abstract class EndPointsParent {

    abstract fun getHostName(): String

    fun getCoinsListWithMarketDataPath(): String {
        return "v3/coins/markets"
    }
}