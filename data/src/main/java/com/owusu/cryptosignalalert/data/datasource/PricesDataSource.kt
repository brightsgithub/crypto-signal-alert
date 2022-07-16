package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper

interface PricesDataSource {
    suspend fun getPrices(ids: String, currencies: String): PriceAPIWrapper
}