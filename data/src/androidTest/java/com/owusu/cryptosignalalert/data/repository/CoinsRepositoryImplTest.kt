package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.test.R
import com.owusu.cryptosignalalert.data.BaseCoreTest
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.StringBuilder
import java.net.URL

class CoinsRepositoryImplTest : BaseCoreTest(), KoinComponent {

    val coinsRepository : CoinsRepository by inject()
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

        initDispatcher(getResponseMapForCoinsListWithMarketData(
            page,
            recordsPerPage,
            currencies,
            R.raw.get_single_coin)
        )

        val coinsList = coinsRepository.getCoinsList(page, recordsPerPage, currencies)

        assert(coinsList.isNotEmpty())
        assert(coinsList.size == recordsPerPage)
        assert(coinsList[0].id != null)
        assert(coinsList[0].name != null)
        assert(coinsList[0].currentPrice!! > 0)
        assert(coinsList[0].symbol != null)
    }

    @Test
    fun getCoinsListTest() = runBlocking {
        val page = 1
        val recordsPerPage = 100
        val currencies = "usd"

        initDispatcher(getResponseMapForCoinsListWithMarketData(
            page,
            recordsPerPage,
            currencies,
            R.raw.get_coins_list_with_market_data)
        )

        val coinsList = coinsRepository.getCoinsList(page, recordsPerPage, currencies)

        assert(coinsList.isNotEmpty())
        assert(coinsList.size == recordsPerPage)
        assert(coinsList[0].id != null)
        assert(coinsList[0].name != null)
        assert(coinsList[0].currentPrice!! > 0)
        assert(coinsList[0].symbol != null)
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