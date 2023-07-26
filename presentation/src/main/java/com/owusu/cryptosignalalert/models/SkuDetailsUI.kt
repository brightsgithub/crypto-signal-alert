package com.owusu.cryptosignalalert.models

data class SkuDetailsUI(
    val sku: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val price: String,
    val isPurchased: Boolean = false,
    val imageResId: Int,
    val purchaseTypeUI: PurchaseTypeUI
)

sealed class PurchaseTypeUI {
    object BuyAll: PurchaseTypeUI()
    object UnlimitedAlerts: PurchaseTypeUI()
    object RemoveAds: PurchaseTypeUI()
}