package com.owusu.cryptosignalalert.data.datasource.coingecko

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.owusu.cryptosignalalert.data.datasource.PricesDataSource
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.models.PriceAPI
import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class CoinGeckoPricesDataSourceImpl(
    private val httpClient: HttpClient,
    private val endPoints: EndPoints,
    private val priceGson: Gson
): PricesDataSource {

    override suspend fun getPrices(ids: String, currencies: String): PriceAPIWrapper {

// https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd&include_market_cap=true&include_24hr_vol=true&include_24hr_change=true&include_last_updated_at=true
        val jsonString: String = httpClient.get(endPoints.getPriceDataPath()) {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            url {
                parameters.append("ids", ids)
                parameters.append("vs_currencies", currencies)
                parameters.append("include_market_cap",true.toString())
                parameters.append("include_24hr_vol",true.toString())
                parameters.append("include_24hr_change",true.toString())
                parameters.append("include_last_updated_at",true.toString())
            }

        }.bodyAsText()

        val priceWrapperApi = priceGson.fromJson(jsonString, PriceAPIWrapper::class.java)
        return priceWrapperApi
    }
}