package com.owusu.cryptosignalalert.data.datasource.db

import com.owusu.cryptosignalalert.data.datasource.CoinIdsLocalDataSource
import com.owusu.cryptosignalalert.data.models.entity.CoinIdEntity
import kotlinx.coroutines.flow.Flow

class CoinIdsLocalDataSourceImpl(
    private val coinIdsDao: CoinIdsDao
): CoinIdsLocalDataSource {

    override suspend fun saveAllCoinIds(coinIdList: List<CoinIdEntity>) {
        coinIdsDao.insertCoinIds(coinIdList)
    }

    override fun searchCoinIds(searchStr: String): Flow<List<CoinIdEntity>> {
        return coinIdsDao.searchCoinIds(searchStr)
    }

    override suspend fun nukeCoinIdsData() {
        coinIdsDao.nukeTable()
    }
}