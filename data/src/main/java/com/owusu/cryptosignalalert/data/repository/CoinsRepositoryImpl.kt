package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.CoinsDataSource
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.models.CoinAPI
import com.owusu.cryptosignalalert.domain.models.Coin
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class CoinsRepositoryImpl(
    private val coinsDataSource: CoinsDataSource,
private val dataAPIListMapper: DataAPIListMapper<CoinAPI, Coin>
): CoinsRepository {
    override suspend fun getCoinsList(
        page: Int,
        recordsPerPage: Int,
        currencies: String
    ): List<Coin> {
        val coinsApiList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies)
        return dataAPIListMapper.mapAPIToDomain(coinsApiList)
    }
}