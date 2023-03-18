package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.api.historic.HistoricPriceAPI
import com.owusu.cryptosignalalert.domain.models.HistoricPriceDomain
import com.owusu.cryptosignalalert.domain.models.HistoricPriceWrapperDomain

class HistoricalPriceAPIToDomainMapper() {
    fun mapToDomain(historicalPriceAPI: HistoricPriceAPI): HistoricPriceWrapperDomain {
        val historicPriceDomains = arrayListOf<HistoricPriceDomain>()
        for(priceApi: List<Double> in historicalPriceAPI.prices) {
            val timestamp = priceApi[0].toLong()
            val price = priceApi[1]
            val historicPriceDomain = HistoricPriceDomain(timestamp = timestamp, price = price)
            historicPriceDomains.add(historicPriceDomain)
        }
        return HistoricPriceWrapperDomain(historicPriceDomains)
    }
}