package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.PriceAPI
import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper
import com.owusu.cryptosignalalert.domain.models.Price
import com.owusu.cryptosignalalert.domain.models.PriceWrapper

class PriceAPIMapper: DataAPIMapper<PriceAPIWrapper, PriceWrapper> {
    override fun mapAPIToDomain(api: PriceAPIWrapper): PriceWrapper {
        val prices = arrayListOf<Price>()
        for (priceApi in api.pricesAPI) {
            priceApi.apply {
                prices.add(
                    Price(
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
        return PriceWrapper(prices)
    }

    override fun mapToDomainApi(domain: PriceWrapper): PriceAPIWrapper {
        val pricesApi = arrayListOf<PriceAPI>()
        for (price in domain.prices) {
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