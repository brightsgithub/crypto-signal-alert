package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.models.SkuDetailsUI

class SkuDetailsDomainToUIMapper {

    fun mapToUI(skuDetailsDomainList: List<SkuDetailsDomain>): List<SkuDetailsUI> {
        val skuUIList = arrayListOf<SkuDetailsUI>()
        skuDetailsDomainList.forEach {
            it.apply {
                skuUIList.add(SkuDetailsUI(
                    sku = sku,
                    title = title,
                    subTitle = subTitle,
                    description = description,
                    price = price,
                    isPurchased = isPurchased
                ))
            }
        }
        return skuUIList
    }
}