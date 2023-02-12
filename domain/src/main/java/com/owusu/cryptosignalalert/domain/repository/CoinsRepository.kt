package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.CoinIdDomain

interface CoinsRepository {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String,
                             ids: String? = null) : List<CoinDomain>

    suspend fun getAllCoinIds(): List<CoinIdDomain>
    suspend fun saveAllCoinIds(coinIds: List<CoinIdDomain>)
    suspend fun searchCoinIds(searchStr: String): List<CoinIdDomain>
    fun hasCoinIdsBeenPopulated(): Boolean
    suspend fun nukeCoinIdsData()
}