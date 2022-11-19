package com.owusu.cryptosignalalert.domain.models

data class SkuDetailsDomain(
    val sku: String,
    val title: String,
    val description: String,
    val price: String,
    val isPurchased: Boolean = false
)
