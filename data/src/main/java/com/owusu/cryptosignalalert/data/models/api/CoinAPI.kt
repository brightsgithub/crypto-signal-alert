package com.owusu.cryptosignalalert.data.models.api


import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.math.BigDecimal

@Serializable
data class CoinAPI(
    @SerialName("ath")
    val ath: Double? = null,
    @SerialName("ath_change_percentage")
    val athChangePercentage: Double? = null,
    @SerialName("ath_date")
    val athDate: String? = null,
    @SerialName("atl")
    val atl: Double? = null,
    @SerialName("atl_change_percentage")
    val atlChangePercentage: Double? = null,
    @SerialName("atl_date")
    val atlDate: String? = null,
    @SerialName("circulating_supply")
    val circulatingSupply: Double? = null,
    @SerialName("current_price")
    val currentPrice: Double? = null,
    @SerialName("fully_diluted_valuation")
    val fullyDilutedValuation: Long? = null,
    @SerialName("high_24h")
    val high24h: Double? = null,
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: String? = null,
    @SerialName("last_updated")
    val lastUpdated: String? = null,
    @SerialName("low_24h")
    val low24h: Double? = null,
    @SerialName("market_cap")
    val marketCap: Double? = null,
    @SerialName("market_cap_change_24h")
    val marketCapChange24h: Double? = null,
    @SerialName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double? = null,
    @SerialName("market_cap_rank")
    val marketCapRank: Double? = null,
    @SerialName("max_supply")
    val maxSupply: Double? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("price_change_24h")
    val priceChange24h: Double? = null,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double? = null,
    @SerialName("symbol")
    val symbol: String? = null,
    @SerialName("total_supply")
    val totalSupply: Double? = null,
    @SerialName("total_volume")
    val totalVolume: Double? = null
)