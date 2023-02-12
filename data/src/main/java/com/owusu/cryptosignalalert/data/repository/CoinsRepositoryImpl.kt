package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.AppPreferences
import com.owusu.cryptosignalalert.data.datasource.CoinIdsLocalDataSource
import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.mappers.CoinIdAPIToDomainIdMapper
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.data.models.api.CoinIdAPI
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.CoinIdDomain
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class CoinsRepositoryImpl(
    private val coinsDataSource: CoinsListDataSource,
    private val dataAPIListMapper: DataAPIListMapper<CoinAPI, CoinDomain>,
    private val coinIdAPIMapper: CoinIdAPIToDomainIdMapper,
    private val coinIdsLocalDataSource: CoinIdsLocalDataSource,
    private val appPreferences: AppPreferences
): CoinsRepository {
    override suspend fun getCoinsList(
        page: Int,
        recordsPerPage: Int,
        currencies: String,
        ids: String?
    ): List<CoinDomain> {
        val coinsApiList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies, ids)
        return dataAPIListMapper.transform(coinsApiList)
    }

    override suspend fun getAllCoinIds(): List<CoinIdDomain> {
        val coinsApiList = coinsDataSource.getAllCoinIds()
        return coinIdAPIMapper.apiToDomain(coinsApiList)
    }

    override suspend fun saveAllCoinIds(coinIds: List<CoinIdDomain>) {
        val entityList = coinIdAPIMapper.domainToEntity(coinIds)
        coinIdsLocalDataSource.saveAllCoinIds(entityList)
        appPreferences.coinIdsHaveBeenPopulated()
    }

    override suspend fun searchCoinIds(searchStr: String): List<CoinIdDomain> {
        val entityList = coinIdsLocalDataSource.searchCoinIds(searchStr)
        return coinIdAPIMapper.entityToDomain(entityList)
    }

    override fun hasCoinIdsBeenPopulated(): Boolean {
        return appPreferences.hasCoinIdsBeenPopulated()
    }

    override suspend fun nukeCoinIdsData() {
        coinIdsLocalDataSource.nukeCoinIdsData()
        appPreferences.coinIdsHaveNotBeenPopulated()
    }
}