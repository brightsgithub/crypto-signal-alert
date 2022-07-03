package com.owusu.cryptosignalalert.domain.models

data class Coin(
    val ath: Int,
    val athChangePercentage: Double,
    val athDate: String,
    val atl: Double,
    val atlChangePercentage: Double,
    val atlDate: String,
    val circulatingSupply: Int,
    val currentPrice: Double,
    val fullyDilutedValuation: Long,
    val high24h: Double,
    val id: String,
    val image: String,
    val lastUpdated: String,
    val low24h: Double,
    val marketCap: Long,
    val marketCapChange24h: Int,
    val marketCapChangePercentage24h: Double,
    val marketCapRank: Int,
    val maxSupply: Int,
    val name: String,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double,
    val symbol: String,
    val totalSupply: Int,
    val totalVolume: Long
)