package com.owusu.cryptosignalalert.data.models.api.coindetail


import com.owusu.cryptosignalalert.data.models.api.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailAPI(
    @SerialName("block_time_in_minutes")
    val blockTimeInMinutes: Int? = null,
    @SerialName("coingecko_rank")
    val coingeckoRank: Int? = null,
    @SerialName("description")
    val description: Description? = null,
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: Image? = null,
    @SerialName("last_updated")
    val lastUpdated: String? = null,
    @SerialName("market_cap_rank")
    val marketCapRank: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("symbol")
    val symbol: String
)