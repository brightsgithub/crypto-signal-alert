package com.owusu.cryptosignalalert.data.datasource.coingecko

import com.owusu.cryptosignalalert.data.BaseCoreTest
import com.owusu.cryptosignalalert.data.test.R
import com.owusu.cryptosignalalert.data.datasource.CoinsListDataSource
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.StringBuilder
import java.net.URL
import java.net.URLEncoder

class CoinGeckoCoinsListDataSourceImplTest : BaseCoreTest(), KoinComponent {

    val coinsDataSource : CoinsListDataSource by inject()
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

    @Test
    fun getCoinsListByIdTest() = runBlocking {

        val page = 1
        val recordsPerPage = 4
        val currencies = "usd"
        val ids = "bitcoin,ethereum,ripple,solana"

        // http://localhost:8080/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=4&page=1&sparkline=false&ids=bitcoin%2Cethereum%2Cripple%2Csolana
        // https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=bitcoin%2Cethereum%2Cripple%2Csolana&order=market_cap_desc&per_page=100&page=1&sparkline=false
        initDispatcher(getResponseMapForCoinsListWithMarketData(page, recordsPerPage, currencies, R.raw.get_coins_list_by_ids, ids))
        val coinsAPIList = coinsDataSource.getCoinsList(page, recordsPerPage, currencies, ids)

        assert(coinsAPIList.isNotEmpty())
        assert(coinsAPIList.size == 4)

        val bitcoin = coinsAPIList[0]
        assert(bitcoin.id.equals("bitcoin"))
        assert(bitcoin.name.equals("Bitcoin"))
        assert(bitcoin.symbol.equals("btc"))
        assert(bitcoin.currentPrice != null)

        val ethereum = coinsAPIList[1]
        assert(ethereum.id.equals("ethereum"))
        assert(ethereum.name.equals("Ethereum"))
        assert(ethereum.symbol.equals("eth"))
        assert(ethereum.currentPrice != null)

        val ripple = coinsAPIList[2]
        assert(ripple.id.equals("ripple"))
        assert(ripple.name.equals("XRP"))
        assert(ripple.symbol.equals("xrp"))
        assert(ripple.currentPrice != null)

        val solana = coinsAPIList[3]
        assert(solana.id.equals("solana"))
        assert(solana.name.equals("Solana"))
        assert(solana.symbol.equals("sol"))
        assert(solana.currentPrice != null)
    }

    private fun getResponseMapForCoinsListWithMarketData(page: Int,
                                                         recordsPerPage: Int,
                                                         currencies: String,
                                                         mockResIdFile: Int,
                                                         ids: String? = null): Map<String, MockResponse> {

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
        ids?.let {
            request.append("&", "ids", "=", URLEncoder.encode(ids, "utf-8"))
        }


        val requestUrl = request.toString()
        val response = getMockedResponse(OK, mockResIdFile)
        // also mock critical RESPONSE headers!
        response.addHeader("content-type", "application/json; charset=utf-8") // critical!
        response.addHeader("cache-control", "public,max-age=300") // optional
        return mapOf(requestUrl to response)
    }
}