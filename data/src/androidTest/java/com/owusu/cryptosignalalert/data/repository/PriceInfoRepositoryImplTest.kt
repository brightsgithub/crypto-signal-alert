package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.BaseCoreTest
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.data.test.R
import com.owusu.cryptosignalalert.domain.repository.PriceInfoRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.StringBuilder
import java.net.URLEncoder

class PriceInfoRepositoryImplTest: BaseCoreTest(), KoinComponent {

    val priceInfoRepository: PriceInfoRepository by inject()
    val endPoints: EndPoints by inject()

    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun getSinglePriceData() = runBlocking {
        val ids = "bitcoin"
        val currencies = "usd"
        initDispatcher(getResponseMapForPriceData(R.raw.get_single_price_data, ids, currencies))

        val priceWrapper = priceInfoRepository.getPrices(ids, currencies)
        val bitcoinPrice = priceWrapper.prices[0]

        assert(bitcoinPrice.id == ids)
        assert(bitcoinPrice.usd > 0)
        assert(bitcoinPrice.lastUpdatedAt > 0)
        assert(bitcoinPrice.usd24hChange > 0)
        assert(bitcoinPrice.usd24hVol > 0)
        assert(bitcoinPrice.usdMarketCap > 0)
    }

    @Test
    fun getMultiPriceData() = runBlocking {
        val bitcoinId = "bitcoin"
        val ethereumId = "ethereum"
        val ids = "$bitcoinId,$ethereumId"
        val currencies = "usd"

        initDispatcher(getResponseMapForPriceData(R.raw.get_multi_price_data, ids, currencies))

        val priceWrapper = priceInfoRepository.getPrices(ids, currencies)
        val bitcoinPrice = priceWrapper.prices[0]
        val ethereumPrice = priceWrapper.prices[1]

        assert(bitcoinPrice.id == bitcoinId)
        assert(bitcoinPrice.usd > 0)
        assert(bitcoinPrice.lastUpdatedAt > 0)
        assert(bitcoinPrice.usd24hChange > 0)
        assert(bitcoinPrice.usd24hVol > 0)
        assert(bitcoinPrice.usdMarketCap > 0)

        assert(ethereumPrice.id == ethereumId)
        assert(ethereumPrice.usd > 0)
        assert(ethereumPrice.lastUpdatedAt > 0)
        assert(ethereumPrice.usd24hChange > 0)
        assert(ethereumPrice.usd24hVol > 0)
        assert(ethereumPrice.usdMarketCap > 0)
    }

    private fun getResponseMapForPriceData(mockResIdFile: Int, ids: String, currencies: String): Map<String, MockResponse> {

        val hostName = endPoints.getHostName()

        // This is a get request, so be sure to include the query string
        val request = StringBuilder()
        request.append(hostName)
        request.append(endPoints.getPriceDataPath())
        request.append("?", "ids", "=", URLEncoder.encode(ids, "utf-8")) // since it will be encoded on mockserver we need to do the same here
        request.append("&", "vs_currencies", "=", currencies) // can also be comma sep
        request.append("&", "include_market_cap", "=", true.toString())
        request.append("&", "include_24hr_vol", "=", true.toString())
        request.append("&", "include_24hr_change", "=", true.toString())
        request.append("&", "include_last_updated_at", "=", true.toString())

        val response = getMockedResponse(OK, mockResIdFile)
        // also mock critical RESPONSE headers!
        response.addHeader("content-type", "application/json; charset=utf-8") // critical!
        response.addHeader("cache-control", "public,max-age=300") // optional


        return mapOf( request.toString() to response)
    }
}