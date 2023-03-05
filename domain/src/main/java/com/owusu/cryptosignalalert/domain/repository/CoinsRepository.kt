package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.CoinDetailDomain
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.CoinIdDomain
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String,
                             ids: String? = null) : List<CoinDomain>

    suspend fun getAllCoinIds(): List<CoinIdDomain>
    suspend fun saveAllCoinIds(coinIds: List<CoinIdDomain>)
    fun searchCoinIds(searchStr: String): Flow<List<CoinIdDomain>>
    fun hasCoinIdsBeenPopulated(): Boolean
    suspend fun nukeCoinIdsData()
    suspend fun getCoinDetail(coinId: String): CoinDetailDomain
}