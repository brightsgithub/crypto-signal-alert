package com.owusu.cryptosignalalert.data.datasource.coingecko

import android.util.Log
import com.owusu.cryptosignalalert.data.datasource.CoinsDataSource
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.models.CoinAPI
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class CoinGeckoDataSourceImpl(
    private val httpClient: HttpClient,
    private val endPoints: EndPoints
    ): CoinsDataSource {

    override suspend fun getCoinsList(
        page: Int,
        recordsPerPage: Int,
        currencies: String
    ) : List<CoinAPI> {

        // vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false


//        val coinApiListStr1: String = httpClient.get(endPoints.getCoinsListWithMarketDataPath()) {
//            // accept(ContentType.Application.Json)
//            // contentType(ContentType.Application.Json)
//            url {
//                parameters.append("vs_currency", currencies)
//                parameters.append("order", "market_cap_desc")
//                parameters.append("per_page", recordsPerPage.toString())
//                parameters.append("page", page.toString())
//                parameters.append("sparkline", false.toString())
//            }
//
//        }.bodyAsText()
//
//        Log.v("CoinGeckoDataSourceImpl", "coinApiListStr1 = "+ coinApiListStr1)
        val coinApiListStr: List<CoinAPI> = httpClient.get(endPoints.getCoinsListWithMarketDataPath()) {
            // accept(ContentType.Application.Json)
            // contentType(ContentType.Application.Json)
            url {
                parameters.append("vs_currency", currencies)
                parameters.append("order", "market_cap_desc")
                parameters.append("per_page", recordsPerPage.toString())
                parameters.append("page", page.toString())
                parameters.append("sparkline", false.toString())
            }

        }.body()

        //val coinApiList = Json{ ignoreUnknownKeys = true }.decodeFromString<List<CoinAPI>>(coinApiListStr)
        return coinApiListStr


//        val coinApiListStr: String = httpClient.get(endPoints.getCoinsListWithMarketDataPath()) {
//            // accept(ContentType.Application.Json)
//            // contentType(ContentType.Application.Json)
//            url {
//                parameters.append("vs_currency", currencies)
//                parameters.append("order", "market_cap_desc")
//                parameters.append("per_page", recordsPerPage.toString())
//                parameters.append("page", page.toString())
//                parameters.append("sparkline", false.toString())
//            }
//
//        }.body()
//
//        val coinApiList = Json{ ignoreUnknownKeys = true }.decodeFromString<List<CoinAPI>>(coinApiListStr)
//        return coinApiList
    }
}