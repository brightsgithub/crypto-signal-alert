package com.owusu.cryptosignalalert.data.enpoints

abstract class EndPointsParent {

    abstract fun getHostName(): String

    fun getCoinsListWithMarketDataPath(): String {
        return "/api/v3/coins/markets"
    }

    fun getPriceDataPath(): String {
        return "/api/v3/simple/price"
    }

    /**
     * Use this to obtain all the coins' id in order to make API calls
     */
    fun getAllCoinsIdListPath(): String {
        return "/api/v3/coins/list"
    }

    fun getCoinDetail(): String {
        return "/api/v3/coins/"
    }

    fun getHistoricalPriceData(): String {
        return "/api/v3/coins/"
    }
}