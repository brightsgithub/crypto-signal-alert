package com.owusu.cryptosignalalert.data.models

import com.android.billingclient.api.ProductDetails

data class SkuWrapper(val skuDetails: ProductDetails, var newPurchasedSku: String? = null)