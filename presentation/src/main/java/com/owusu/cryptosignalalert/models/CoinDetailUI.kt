package com.owusu.cryptosignalalert.models

import com.owusu.cryptosignalalert.domain.models.ImageDomain

data class CoinDetailUI(val blockTimeInMinutes: Int? = null,
                        val coingeckoRank: Int? = null,
                        val description: String? = null,
                        val id: String,
                        val image: ImageUI? = null,
                        val lastUpdated: String? = null,
                        val marketCapRank: Int? = null,
                        val name: String,
                        val symbol: String)
