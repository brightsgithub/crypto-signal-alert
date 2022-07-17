package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.PriceWrapper

interface PriceInfoRepository {
    suspend fun getPrices(ids: String, currencies: String): PriceWrapper
}