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

    //lateinit var coinsDataSource: CoinsDataSource
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
    fun getCoinsListWithMarketData() = runBlocking {

        val page = 1
        val recordsPerPage = 100
        val currencies = "usd"

        initDispatcher(getResponseMapForCoinsListWithMarketData(page, recordsPerPage, currencies))
        val coinsAPIList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies)

        assert(coinsAPIList.isNotEmpty())
    }

    private fun getResponseMapForCoinsListWithMarketData(page: Int,
                                                         recordsPerPage: Int,
                                                         currencies: String): Map<String, MockResponse> {

        val hostName = endPoints.getHostName()

        // This is a get request, so be sure to include the query string
        val request = StringBuilder()
        request.append(hostName)
        request.append(endPoints.getCoinsListWithMarketDataPath())
        request.append("vs_currency", currencies)
        request.append("order", "market_cap_desc")
        request.append("per_page", recordsPerPage.toString())
        request.append("page", page.toString())
        request.append("sparkline", false.toString())
        val response = getMockedResponse(OK, R.raw.get_coins_list_with_market_data)
        return mapOf(request.toString() to response)
    }
}