package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.api.PriceAPI
import com.owusu.cryptosignalalert.data.models.api.PriceAPIWrapper
import com.owusu.cryptosignalalert.domain.models.PriceDomain
import com.owusu.cryptosignalalert.domain.models.PriceWrapperDomain

class PriceAPIMapper: DataMapper<PriceAPIWrapper, PriceWrapperDomain> {
    override fun transform(objA: PriceAPIWrapper): PriceWrapperDomain {
        val prices = arrayListOf<PriceDomain>()
        for (priceApi in objA.pricesAPI) {
            priceApi.apply {
                prices.add(
                    PriceDomain(
                        id = id!!,
                        lastUpdatedAt = lastUpdatedAt,
                        usd = usd,
                        usd24hChange = usd24hChange,
                        usd24hVol = usd24hVol,
                        usdMarketCap = usdMarketCap
                    )
                )
            }
        }
        return PriceWrapperDomain(prices)
    }

    override fun reverseTransformation(objB: PriceWrapperDomain): PriceAPIWrapper {
        val pricesApi = arrayListOf<PriceAPI>()
        for (price in objB.domainPrices) {
            price.apply {
                pricesApi.add(
                    PriceAPI(
                        id = id,
                        lastUpdatedAt = lastUpdatedAt,
                        usd = usd,
                        usd24hChange = usd24hChange,
                        usd24hVol = usd24hVol,
                        usdMarketCap = usdMarketCap
                    )
                )
            }
        }
        return PriceAPIWrapper(pricesApi)
    }
}