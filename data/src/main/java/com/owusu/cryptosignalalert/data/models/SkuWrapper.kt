package com.owusu.cryptosignalalert.data.models

import com.android.billingclient.api.SkuDetails

data class SkuWrapper(val skuDetails: SkuDetails, var newPurchasedSku: String? = null)