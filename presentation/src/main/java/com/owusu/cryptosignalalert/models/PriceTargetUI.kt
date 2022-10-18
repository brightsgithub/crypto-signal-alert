package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import com.owusu.cryptosignalalert.mappers.PriceTargetDirectionUI
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceTargetUI(
    val localPrimeId: Int,
    val id: String,
    val ath: Double? = null,
    val athChangePercentage: Double? = null,
    val athDate: String? = null,
    val atl: Double? = null,
    val atlChangePercentage: Double? = null,
    val atlDate: String? = null,
    val circulatingSupply: Double? = null,
    val currentPrice: Double? = null,
    val currentPriceDisplay: String? = null,
    val fullyDilutedValuation: Double? = null,
    val high24h: Double? = null,
    val image: String? = null,
    val lastUpdated: String? = null,
    val low24h: Double? = null,
    val marketCap: Double? = null,
    val marketCapChange24h: Double? = null,
    val marketCapChangePercentage24h: Double? = null,
    val marketCapRank: Int? = null,
    val maxSupply: Double? = null,
    val name: String? = null,
    val priceChange24h: Double? = null,
    val priceChangePercentage24h: Double? = null,
    val symbol: String? = null,
    val totalSupply: Double? = null,
    val totalVolume: Double? = null,
    val userPriceTarget: Double? = null,
    val userPriceTargetDisplay: String? = null,
    val hasPriceTargetBeenHit: Boolean = false,
    val hasUserBeenAlerted: Boolean = false,
    val priceTargetDirection: PriceTargetDirectionUI = PriceTargetDirectionUI.NOT_SET,
    val progress: Float = 0f,
    val progressPercentageDisplay: String? = null,
): Parcelable