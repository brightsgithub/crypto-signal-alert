package com.owusu.cryptosignalalert.data.datasource.coingecko

import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.data.models.api.CoinIdAPI
import com.owusu.cryptosignalalert.data.models.api.coindetail.CoinDetailAPI
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CoinGeckoCoinsListDataSourceImpl(
    private val httpClient: HttpClient,
    private val endPoints: EndPoints
    ): CoinsListDataSource {

    override suspend fun getCoinsList(
        page: Int,
        recordsPerPage: Int,
        currencies: String,
        ids: String?
    ) : List<CoinAPI> {

        val coinApiListStr: List<CoinAPI> = httpClient.get(endPoints.getCoinsListWithMarketDataPath()) {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            url {
                parameters.append("vs_currency", currencies)
                parameters.append("order", "market_cap_desc")
                parameters.append("per_page", recordsPerPage.toString())
                parameters.append("page", page.toString())
                parameters.append("sparkline", false.toString())
                ids?.let {
                    parameters.append("ids", it)
                }
            }

        }.body()
        return coinApiListStr
    }

    override suspend fun getAllCoinIds(): List<CoinIdAPI> {
        val result: List<CoinIdAPI> = httpClient.get(endPoints.getAllCoinsIdListPath()) {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            url {
                parameters.append("include_platform", false.toString())
            }
        }.body()
        return result
    }

    override suspend fun getCoinDetail(coinId: String): CoinDetailAPI {

        val result: CoinDetailAPI = httpClient.get(endPoints.getCoinDetail() + coinId) {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            url {
                parameters.append("localization", false.toString())
                parameters.append("tickers", false.toString())
                parameters.append("market_data", false.toString())
                parameters.append("community_data", false.toString())
                parameters.append("developer_data", false.toString())
                parameters.append("sparkline", false.toString())
            }

        }.body()
        return result
    }
}