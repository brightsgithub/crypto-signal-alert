package com.owusu.cryptosignalalert.data.models.skus

import com.owusu.cryptosignalalert.domain.models.ProductKeys

object Skus {
    val SKU_UNLOCK_ALL = "com.owusu.cryptosignalalert.unlock_all"
    val SKU_UNLIMITED_ALERTS = "com.owusu.cryptosignalalert.unlimted_alerts"
    val SKU_REMOVE_ADS = "com.owusu.cryptosignalalert.remove_ads"
    val INAPP_SKUS = arrayOf(SKU_UNLOCK_ALL, SKU_UNLIMITED_ALERTS, SKU_REMOVE_ADS)
    val INAPP_SKUS_MAP = mapOf(
        ProductKeys.UNLOCK_ALL to SKU_UNLOCK_ALL,
        ProductKeys.UNLIMITED_ALERTS to SKU_UNLIMITED_ALERTS,
        ProductKeys.REMOVE_ADS to SKU_REMOVE_ADS
    )
}