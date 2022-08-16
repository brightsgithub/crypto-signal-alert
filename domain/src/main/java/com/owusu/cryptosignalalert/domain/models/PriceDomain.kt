package com.owusu.cryptosignalalert.domain.models

data class PriceDomain(
    val id: String,
    val lastUpdatedAt: Double,
    val usd: Double,
    val usd24hChange: Double,
    val usd24hVol: Double,
    val usdMarketCap: Double
)
