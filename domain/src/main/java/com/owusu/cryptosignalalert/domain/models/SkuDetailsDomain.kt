package com.owusu.cryptosignalalert.domain.models

data class SkuDetailsDomain(
    val pos: Int,
    val sku: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val price: String,
    val isPurchased: Boolean = false
)
