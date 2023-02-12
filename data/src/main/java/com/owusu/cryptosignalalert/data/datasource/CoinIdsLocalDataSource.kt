package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.data.models.entity.CoinIdEntity

interface CoinIdsLocalDataSource {
    suspend fun saveAllCoinIds(coinIdList: List<CoinIdEntity>)
    suspend fun searchCoinIds(searchStr: String): List<CoinIdEntity>
    suspend fun nukeCoinIdsData()
}