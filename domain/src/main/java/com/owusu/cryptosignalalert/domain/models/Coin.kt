package com.owusu.cryptosignalalert.domain.models

data class Coin(
    val ath: Int,
    val ath_change_percentage: Double,
    val ath_date: String,
    val atl: Double,
    val atl_change_percentage: Double,
    val atl_date: String,
    val circulating_supply: Int,
    val current_price: Double,
    val fully_diluted_valuation: Long,
    val high_24h: Double,
    val id: String,
    val image: String,
    val last_updated: String,
    val low_24h: Double,
    val market_cap: Long,
    val market_cap_change_24h: Int,
    val market_cap_change_percentage_24h: Double,
    val market_cap_rank: Int,
    val max_supply: Int,
    val name: String,
    val price_change_24h: Double,
    val price_change_percentage_24h: Double,
    val symbol: String,
    val total_supply: Int,
    val total_volume: Long
)