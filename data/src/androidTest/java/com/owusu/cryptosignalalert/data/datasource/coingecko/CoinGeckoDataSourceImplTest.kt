package com.owusu.cryptosignalalert.data.datasource.coingecko

import com.owusu.cryptosignalalert.data.BaseCoreTest
import com.owusu.cryptosignalalert.data.test.R
import com.owusu.cryptosignalalert.data.datasource.CoinsDataSource
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.StringBuilder

class CoinGeckoDataSourceImplTest : BaseCoreTest(), KoinComponent {

    val coinsDataSource : CoinsDataSource by inject()
    val endPoints : EndPoints by inject()

    @Before
    override fun setUp() {
        super.setUp()
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun getSingleItemInListWithMarketDataTest() = runBlocking {

        val page = 1
        val recordsPerPage = 1
        val currencies = "usd"

        // http://localhost:8080/api/v3/coins/marketsvs_currencyusdordermarket_cap_descper_page1page1sparklinefalse
        // https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=1&page=1&sparkline=false
        initDispatcher(getResponseMapForCoinsListWithMarketData(page, recordsPerPage, currencies, R.raw.get_single_coin))
        val coinsAPIList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies)

        assert(coinsAPIList.isNotEmpty())
        assert(coinsAPIList.size == recordsPerPage)
        assert(coinsAPIList[0].id != null)
        assert(coinsAPIList[0].name != null)
        assert(coinsAPIList[0].currentPrice != null)
    }

    @Test
    fun getCoinsListWithMarketDataTest() = runBlocking {

        val page = 1
        val recordsPerPage = 100
        val currencies = "usd"

        // http://localhost:8080/api/v3/coins/marketsvs_currencyusdordermarket_cap_descper_page100page1sparklinefalse
        // https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false
        initDispatcher(getResponseMapForCoinsListWithMarketData(page, recordsPerPage, currencies, R.raw.get_coins_list_with_market_data))
        val coinsAPIList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies)

        assert(coinsAPIList.isNotEmpty())
        assert(coinsAPIList.size == recordsPerPage)
        assert(coinsAPIList[0].id != null)
        assert(coinsAPIList[0].name != null)
        assert(coinsAPIList[0].currentPrice != null)
    }

    private fun getResponseMapForCoinsListWithMarketData(page: Int,
                                                         recordsPerPage: Int,
                                                         currencies: String,
                                                         mockResIdFile: Int): Map<String, MockResponse> {

        val hostName = endPoints.getHostName()

        // This is a get request, so be sure to include the query string
        val request = StringBuilder()
        request.append(hostName)
        request.append(endPoints.getCoinsListWithMarketDataPath())
        request.append("?", "vs_currency", "=", currencies)
        request.append("&", "order", "=", "market_cap_desc")
        request.append("&", "per_page", "=", recordsPerPage.toString())
        request.append("&", "page", "=", page.toString())
        request.append("&", "sparkline", "=", false.toString())

        val response = getMockedResponse(OK, mockResIdFile)
        // also mock critical RESPONSE headers!
        response.addHeader("content-type", "application/json; charset=utf-8") // critical!
        response.addHeader("cache-control", "public,max-age=300") // optional
        return mapOf(request.toString() to response)
    }
}