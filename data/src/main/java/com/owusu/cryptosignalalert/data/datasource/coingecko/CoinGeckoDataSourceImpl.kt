package com.owusu.cryptosignalalert.data.datasource.coingecko

import com.owusu.cryptosignalalert.data.datasource.CoinsDataSource
import com.owusu.cryptosignalalert.data.models.CoinAPI

class CoinGeckoDataSourceImpl: CoinsDataSource {
    override suspend fun getCoinsList(page: Int, recordsPerPage: Int) : CoinAPI {

    }
}