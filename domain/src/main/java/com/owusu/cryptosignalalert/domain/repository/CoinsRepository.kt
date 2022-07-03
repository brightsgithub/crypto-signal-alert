package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.Coin

interface CoinsRepository {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String) : List<Coin>
}