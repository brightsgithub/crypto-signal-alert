package com.owusu.cryptosignalalert.data.datasource.coingecko

import com.owusu.cryptosignalalert.data.datasource.CoinsDataSource
import com.owusu.cryptosignalalert.data.models.CoinAPI
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class CoinGeckoDataSourceImpl(private val httpClient: HttpClient): CoinsDataSource {

    override suspend fun getCoinsList(
        page: Int,
        recordsPerPage: Int,
        currencies: String
    ) : List<CoinAPI> {

        //val customer: Customer = client.get("http://localhost:8080/customer/3").body()

        // vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false
        val coinApiList: List<CoinAPI> = httpClient.get("v3/coins/markets") {
            url {
                parameters.append("vs_currency", currencies)
                parameters.append("order", "market_cap_desc")
                parameters.append("per_page", recordsPerPage.toString())
                parameters.append("page", page.toString())
                parameters.append("sparkline", false.toString())
            }
        }.body()
        return coinApiList
    }
}