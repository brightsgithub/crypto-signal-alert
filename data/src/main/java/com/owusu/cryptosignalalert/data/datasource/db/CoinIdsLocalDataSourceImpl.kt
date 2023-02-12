package com.owusu.cryptosignalalert.data.datasource.db

import com.owusu.cryptosignalalert.data.datasource.CoinIdsLocalDataSource
import com.owusu.cryptosignalalert.data.models.entity.CoinIdEntity

class CoinIdsLocalDataSourceImpl(
    private val coinIdsDao: CoinIdsDao
): CoinIdsLocalDataSource {

    override suspend fun saveAllCoinIds(coinIdList: List<CoinIdEntity>) {
        coinIdsDao.insertCoinIds(coinIdList)
    }

    override suspend fun searchCoinIds(searchStr: String): List<CoinIdEntity> {
        return coinIdsDao.searchCoinIds(searchStr)
    }

    override suspend fun nukeCoinIdsData() {
        coinIdsDao.nukeTable()
    }
}