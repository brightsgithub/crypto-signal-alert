package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.CoinDomain

interface CoinsRepository {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String,
                             ids: String? = null) : List<CoinDomain>
}