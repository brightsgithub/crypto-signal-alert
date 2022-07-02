package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.data.models.CoinAPI

interface CoinsDataSource {
    suspend fun getCoinsList(page: Int, recordsPerPage: Int) : CoinAPI
}