package com.owusu.cryptosignalalert.domain.models

sealed class CoinsListResult() {
    data class Success(var coinDomainList: List<CoinDomain>): CoinsListResult()
    data class Error(var coinsListResultErrorType: CoinsListResultErrorType): CoinsListResult()
}

sealed class CoinsListResultErrorType() {
    object RateLimitReached: CoinsListResultErrorType()
    // other errors
}