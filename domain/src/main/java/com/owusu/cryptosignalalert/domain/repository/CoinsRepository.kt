package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.Coin
import com.owusu.cryptosignalalert.domain.models.PriceWrapper

interface CoinsRepository {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String) : List<Coin>

    suspend fun getPrices(ids: String, currencies: String): PriceWrapper
}