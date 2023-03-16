package com.owusu.cryptosignalalert.domain.models

data class HistoricPriceWrapperDomain(val prices: List<HistoricPriceDomain>)

data class HistoricPriceDomain(val timestamp: Long, val price: Double)
