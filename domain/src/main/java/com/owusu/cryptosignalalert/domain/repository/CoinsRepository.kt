package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.*
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String,
                             ids: String? = null) : CoinsListResult

    suspend fun getAllCoinIds(): List<CoinIdDomain>
    suspend fun saveAllCoinIds(coinIds: List<CoinIdDomain>)
    fun searchCoinIds(searchStr: String): Flow<List<CoinIdDomain>>
    suspend fun nukeCoinIdsData()
    suspend fun getCoinDetail(coinId: String): CoinDetailDomain
    suspend fun getHistoricalPriceData(coinId: String, currency: String): HistoricPriceWrapperDomain
    fun setLastCoinIdUpdate(timestamp: Long)
    fun getLastCoinIdUpdate(): Long
}