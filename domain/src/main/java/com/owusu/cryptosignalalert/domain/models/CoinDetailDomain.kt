package com.owusu.cryptosignalalert.domain.models

data class CoinDetailDomain(
    val blockTimeInMinutes: Int? = null,
    val coingeckoRank: Int? = null,
    val description: String? = null,
    val id: String,
    val image: ImageDomain? = null,
    val lastUpdated: String? = null,
    val marketCapRank: Int? = null,
    val name: String,
    val symbol: String
    )
