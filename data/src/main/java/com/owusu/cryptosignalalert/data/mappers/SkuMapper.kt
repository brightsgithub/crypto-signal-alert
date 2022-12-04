package com.owusu.cryptosignalalert.data.mappers

import com.android.billingclient.api.SkuDetails
import com.owusu.cryptosignalalert.data.models.skus.Skus
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain

class SkuMapper {
    fun transform(skuDetails: SkuDetails, isPurchased: Boolean, skus: Skus): SkuDetailsDomain {
        skuDetails.let {
            it.apply {
                return createSkuDetailsDomain(skuDetails, skus, isPurchased)
            }
        }
    }

    private fun createSkuDetailsDomain(skuDetails: SkuDetails, skus: Skus, isPurchased: Boolean): SkuDetailsDomain {
        return when (skuDetails.sku) {
            skus.SKU_UNLIMITED_ALERTS -> {
                SkuDetailsDomain(
                    pos = 1,
                    sku = skuDetails.sku,
                    title = "Set unlimited alerts",
                    subTitle = "Unlimited alerts",
                    description = skuDetails.description,
                    price = skuDetails.price,
                    isPurchased = isPurchased,
                    isBundleBuyAll = false
                )
            }
            skus.SKU_REMOVE_ADS -> {
                SkuDetailsDomain(
                    pos = 2,
                    sku = skuDetails.sku,
                    title = "Remove Ads",
                    subTitle = "Remove those annoying ads",
                    description = skuDetails.description,
                    price = skuDetails.price,
                    isPurchased = isPurchased,
                    isBundleBuyAll = false
                )
            }
            else -> {
                SkuDetailsDomain(
                    pos = 0,
                    sku = skuDetails.sku,
                    title = "Buy All Bundle",
                    subTitle = "Purchase all discount",
                    description = skuDetails.description,
                    price = skuDetails.price,
                    isPurchased = isPurchased,
                    isBundleBuyAll = true
                )
            }
        }
    }
}