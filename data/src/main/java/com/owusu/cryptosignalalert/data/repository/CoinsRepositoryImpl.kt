package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.CoinIdsLocalDataSource
import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.mappers.CoinDetailAPIToDomainMapper
import com.owusu.cryptosignalalert.data.mappers.CoinIdAPIToDomainIdMapper
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.mappers.HistoricalPriceAPIToDomainMapper
import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.domain.models.CoinDetailDomain
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.CoinIdDomain
import com.owusu.cryptosignalalert.domain.models.HistoricPriceWrapperDomain
import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinsRepositoryImpl(
    private val coinsDataSource: CoinsListDataSource,
    private val dataAPIListMapper: DataAPIListMapper<CoinAPI, CoinDomain>,
    private val coinIdAPIMapper: CoinIdAPIToDomainIdMapper,
    private val coinIdsLocalDataSource: CoinIdsLocalDataSource,
    private val appPreferences: AppPreferencesRepository,
    private val coinDetailAPIToDomainMapper: CoinDetailAPIToDomainMapper,
    private val historicalPriceAPIToDomainMapper: HistoricalPriceAPIToDomainMapper,

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
    }

    override fun searchCoinIds(searchStr: String): Flow<List<CoinIdDomain>> {
        val entityList = coinIdsLocalDataSource.searchCoinIds(searchStr)
        return entityList.map {
            coinIdAPIMapper.entityToDomain(it)
        }
    }

    override fun setLastCoinIdUpdate(timestamp: Long) {
        appPreferences.setLastCoinIdUpdate(timestamp)
    }

    override fun getLastCoinIdUpdate(): Long {
        return appPreferences.getLastCoinIdUpdate()
    }

    override suspend fun nukeCoinIdsData() {
        coinIdsLocalDataSource.nukeCoinIdsData()
    }

    override suspend fun getCoinDetail(coinId: String): CoinDetailDomain {
        val coinDetail = coinsDataSource.getCoinDetail(coinId)
        return coinDetailAPIToDomainMapper.mapToDomain(coinDetail)
    }

    override suspend fun getHistoricalPriceData(coinId: String, currency: String): HistoricPriceWrapperDomain {
        val result = coinsDataSource.getHistoricalPriceData(coinId, currency)
        return historicalPriceAPIToDomainMapper.mapToDomain(result)
    }
}