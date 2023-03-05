package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.data.models.entity.CoinIdEntity
import kotlinx.coroutines.flow.Flow

interface CoinIdsLocalDataSource {
    suspend fun saveAllCoinIds(coinIdList: List<CoinIdEntity>)
    fun searchCoinIds(searchStr: String): Flow<List<CoinIdEntity>>
    suspend fun nukeCoinIdsData()
}