package com.owusu.cryptosignalalert.data.mappers

import com.android.billingclient.api.SkuDetails
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain

class SkuMapper {
    fun transform(skuDetails: SkuDetails, isPurchased: Boolean): SkuDetailsDomain {
        skuDetails.let {
            it.apply {
                return SkuDetailsDomain(
                    sku = sku,
                    title = title,
                    description = description,
                    price = price,
                    isPurchased = isPurchased
                )
            }
        }
    }
}