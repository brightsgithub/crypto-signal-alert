package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.domain.models.PurchaseType
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.models.PurchaseTypeUI
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
                    isPurchased = isPurchased,
                    purchaseTypeUI = getPurchaseTypeUI(purchaseType),
                    imageResId = getIconId(purchaseType)
                ))
            }
        }
        return skuUIList
    }

    private fun getPurchaseTypeUI(purchaseType: PurchaseType): PurchaseTypeUI {
        return when (purchaseType) {
            PurchaseType.BuyAll -> PurchaseTypeUI.BuyAll
            PurchaseType.RemoveAds -> PurchaseTypeUI.RemoveAds
            PurchaseType.UnlimitedAlerts -> PurchaseTypeUI.UnlimitedAlerts
        }
    }

    private fun getIconId(purchaseType: PurchaseType): Int {
        return when (purchaseType) {
            PurchaseType.BuyAll -> R.drawable.noads
            PurchaseType.RemoveAds -> R.drawable.noads
            PurchaseType.UnlimitedAlerts -> R.drawable.ic_alart_set
        }
    }
}