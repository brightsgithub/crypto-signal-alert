package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class CoinsRepositoryImpl(
    private val coinsDataSource: CoinsListDataSource,
    private val dataAPIListMapper: DataAPIListMapper<CoinAPI, CoinDomain>
): CoinsRepository {

    override suspend fun getCoinsList(
        page: Int,
        recordsPerPage: Int,
        currencies: String,
        ids: String?
    ): List<CoinDomain> {
        val coinsApiList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies, ids)
        return dataAPIListMapper.mapAPIToDomain(coinsApiList)
    }
}