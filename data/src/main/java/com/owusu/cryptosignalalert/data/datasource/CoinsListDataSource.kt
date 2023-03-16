package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.data.models.api.CoinIdAPI
import com.owusu.cryptosignalalert.data.models.api.coindetail.CoinDetailAPI
import com.owusu.cryptosignalalert.data.models.api.historic.HistoricPriceAPI
import com.owusu.cryptosignalalert.domain.models.CoinIdDomain

interface CoinsListDataSource {

    suspend fun getCoinsList(page: Int,
                             recordsPerPage: Int,
                             currencies: String,
                             ids: String? =  null) : List<CoinAPI>

    suspend fun getAllCoinIds(): List<CoinIdAPI>
    suspend fun getCoinDetail(coinId: String): CoinDetailAPI
    suspend fun getHistoricalPriceData(coinId: String, currency: String): HistoricPriceAPI
}