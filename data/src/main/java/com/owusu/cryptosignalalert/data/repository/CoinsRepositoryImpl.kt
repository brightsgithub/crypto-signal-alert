package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.datasource.PricesDataSource
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.mappers.DataAPIMapper
import com.owusu.cryptosignalalert.data.models.CoinAPI
import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper
import com.owusu.cryptosignalalert.domain.models.Coin
import com.owusu.cryptosignalalert.domain.models.Price
import com.owusu.cryptosignalalert.domain.models.PriceWrapper
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class CoinsRepositoryImpl(
    private val coinsDataSource: CoinsListDataSource,
    private val dataAPIListMapper: DataAPIListMapper<CoinAPI, Coin>
): CoinsRepository {

    override suspend fun getCoinsList(
        page: Int,
        recordsPerPage: Int,
        currencies: String,
        ids: String?
    ): List<Coin> {
        val coinsApiList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies, ids)
        return dataAPIListMapper.mapAPIToDomain(coinsApiList)
    }
}