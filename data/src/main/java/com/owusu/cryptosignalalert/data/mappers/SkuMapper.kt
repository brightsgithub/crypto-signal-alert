package com.owusu.cryptosignalalert.data.mappers

import com.android.billingclient.api.SkuDetails
import com.owusu.cryptosignalalert.data.models.SkuWrapper
import com.owusu.cryptosignalalert.data.models.skus.Skus
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.NewPurchasesState

class SkuMapper {
    fun transform(skuWrapper: SkuWrapper, isPurchased: Boolean, skus: Skus): SkuDetailsDomain {
        val skuDetails = skuWrapper.skuDetails

        val currentPurchasedState = getCurrentPurchasedState(skuWrapper, isPurchased)
        skuDetails.let {
            it.apply {
                return createSkuDetailsDomain(skuDetails, skus, currentPurchasedState)
            }
        }
    }

    private fun getCurrentPurchasedState(skuWrapper: SkuWrapper, isPurchased: Boolean): Boolean {
        val newPurchasedSku = skuWrapper.newPurchasedSku
        return if (newPurchasedSku == null) {
            isPurchased
        } else if (newPurchasedSku!!.equals(skuWrapper.skuDetails.sku)) {
            true // it means there has just been a purchase
        } else  {
            return isPurchased
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