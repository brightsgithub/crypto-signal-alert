package com.owusu.cryptosignalalert.domain.models

data class SkuDetailsDomain(
    val pos: Int,
    val sku: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val price: String,
    var isPurchased: Boolean = false,
    val isBundleBuyAll: Boolean,
    val purchaseType: PurchaseType
)

sealed class PurchaseType {
    object BuyAll: PurchaseType()
    object UnlimitedAlerts: PurchaseType()
    object RemoveAds: PurchaseType()
}
