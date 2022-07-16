package com.owusu.cryptosignalalert.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceAPI(
    @SerialName("last_updated_at")
    val lastUpdatedAt: Double,
    @SerialName("usd")
    val usd: Double,
    @SerialName("usd_24h_change")
    val usd24hChange: Double,
    @SerialName("usd_24h_vol")
    val usd24hVol: Double,
    @SerialName("usd_market_cap")
    val usdMarketCap: Double
)