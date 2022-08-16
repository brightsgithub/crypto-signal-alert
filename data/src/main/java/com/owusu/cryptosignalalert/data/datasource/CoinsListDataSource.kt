package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.data.models.api.CoinAPI

interface CoinsListDataSource {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String,
                             ids: String? =  null) : List<CoinAPI>
}