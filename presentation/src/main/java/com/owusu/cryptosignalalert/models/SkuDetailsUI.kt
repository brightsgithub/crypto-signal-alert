package com.owusu.cryptosignalalert.models

data class SkuDetailsUI(
    val sku: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val price: String,
    val isPurchased: Boolean = false
)