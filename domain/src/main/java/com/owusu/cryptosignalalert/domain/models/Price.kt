package com.owusu.cryptosignalalert.domain.models

data class Price(
    val lastUpdatedAt: Double,
    val usd: Double,
    val usd24hChange: Double,
    val usd24hVol: Double,
    val usdMarketCap: Double
)
