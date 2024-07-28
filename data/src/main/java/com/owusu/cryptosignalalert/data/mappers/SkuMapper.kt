package com.owusu.cryptosignalalert.data.mappers

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.owusu.cryptosignalalert.data.models.SkuWrapper
import com.owusu.cryptosignalalert.data.models.skus.Skus
import com.owusu.cryptosignalalert.domain.models.PurchaseType
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain

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
        } else if (newPurchasedSku!!.equals(skuWrapper.skuDetails.productId)) {
            true // it means there has just been a purchase
        } else  {
            return isPurchased
        }
    }

    private fun createSkuDetailsDomain(productDetails: ProductDetails, skus: Skus, isPurchased: Boolean): SkuDetailsDomain {
        return when (productDetails.productId) {
            skus.SKU_UNLIMITED_ALERTS -> {
                SkuDetailsDomain(
                    pos = 1,
                    sku = productDetails.productId,
                    title = "Unlimited Alerts",
                    subTitle = "Unlimited alerts",
                    description = productDetails.description,
                    price = getPrice(productDetails),
                    isPurchased = isPurchased,
                    isBundleBuyAll = false,
                    purchaseType = PurchaseType.UnlimitedAlerts
                )
            }
            skus.SKU_REMOVE_ADS -> {
                SkuDetailsDomain(
                    pos = 2,
                    sku = productDetails.productId,
                    title = "Remove Ads",
                    subTitle = "Remove those annoying ads",
                    description = productDetails.description,
                    price = getPrice(productDetails),
                    isPurchased = isPurchased,
                    isBundleBuyAll = false,
                    purchaseType = PurchaseType.RemoveAds
                )
            }
            else -> {
                SkuDetailsDomain(
                    pos = 0,
                    sku = productDetails.productId,
                    title = "Buy All bundle",
                    subTitle = "Purchase all discount",
                    description = productDetails.description,
                    price = getPrice(productDetails),
                    isPurchased = isPurchased,
                    isBundleBuyAll = true,
                    purchaseType = PurchaseType.BuyAll
                )
            }
        }
    }

    private fun getPrice(productDetails: ProductDetails): String {
        return if (productDetails.productType == BillingClient.ProductType.INAPP) {
            productDetails.oneTimePurchaseOfferDetails?.formattedPrice?:""
        } else {
            // TODO manage subscription type
            ""
        }
    }
}