package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


/**
 * Compose is tracking for the MutableList are changes related to adding and removing elements.
 * This is why deleting works. But it's unaware of changes in the row item values
 * There are two ways to fix this:

1) Change our data class CoinUI so that checkedState becomes MutableState<Boolean> instead of
Boolean, which causes Compose to track an item change.

2) Copy the item you're about to mutate, remove
the item from your list and re-add the mutated item to the list, which causes Compose to track that list change.
There are pros and cons to both approaches. For example, depending on your implementation of the
list you're using, removing and reading the element might be costly.

So let's say, you want to avoid potentially expensive list operations, and make checkedState
observable as it's more efficient and Compose-idiomatic.
https://developer.android.com/codelabs/jetpack-compose-state?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fjetpack-compose-for-android-developers-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-state#11
 */

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
    val totalVolume: Double? = null,
    val hasPriceTarget: @RawValue MutableState<Boolean> = mutableStateOf(false),
    var userPriceTarget: Double? = null,
    var userPriceTargetDisplay: String? = null
): Parcelable
