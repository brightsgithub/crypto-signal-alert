package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoinUI(
    val ath: Double? = null,
    val athChangePercentage: Double? = null,
    val athDate: String? = null,
    val atl: Double? = null,
    val atlChangePercentage: Double? = null,
    val atlDate: String? = null,
    val circulatingSupply: Double? = null,
    val currentPrice: Double? = null,
    val currentPriceStr: String? = null,
    val fullyDilutedValuation: Double? = null,
    val high24h: Double? = null,
    val id: String,
    val image: String? = null,
    val lastUpdated: String? = null,
    val low24h: Double? = null,
    val marketCap: Double? = null,
    val marketCapStr: String? = null,
    val marketCapChange24h: Double? = null,
    val marketCapChangePercentage24h: Double? = null,
    val priceChangePercentage24hStr: String? = null,
    val is24HrPriceChangePositive: Boolean = false,
    val marketCapRank: Int? = null,
    val maxSupply: Double? = null,
    val name: String? = null,
    val priceChange24h: Double? = null,
    val priceChangePercentage24h: Double? = null,
    val symbol: String? = null,
    val totalSupply: Double? = null,
    val totalVolume: Double? = null
): Parcelable
