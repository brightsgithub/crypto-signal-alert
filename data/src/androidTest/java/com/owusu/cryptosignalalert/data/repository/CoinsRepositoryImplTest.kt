package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.test.R
import com.owusu.cryptosignalalert.data.BaseCoreTest
import com.owusu.cryptosignalalert.data.endpoints.EndPoints
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

import java.lang.StringBuilder
import java.net.URL
import java.net.URLEncoder

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

    @Test
    fun getCoinsListByIdTest() = runBlocking {

        val page = 1
        val recordsPerPage = 4
        val currencies = "usd"
        val ids = "bitcoin,ethereum,ripple,solana"

        initDispatcher(getResponseMapForCoinsListWithMarketData(page, recordsPerPage, currencies, R.raw.get_coins_list_by_ids, ids))
        val coinsAPIList = coinsRepository.getCoinsList(page, recordsPerPage, currencies, ids)

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

    @Test
    fun getCoinIdsFromRemoteTest() = runBlocking {

        initDispatcher(getResponseMapForCoinIds(R.raw.get_all_coin_ids))

        val coinsList = coinsRepository.getAllCoinIds()

        assert(coinsList.isNotEmpty())

        assert(coinsList[0].id != null)
        assert(coinsList[0].name != null)
        assert(coinsList[0].symbol != null)
    }

    @Test
    fun getCoinDetailTest() = runBlocking {

        val coinId = "bitcoin"
        initDispatcher(getResponseForCoinDetail(R.raw.get_coin_detail, coinId))

        val coinDetail = coinsRepository.getCoinDetail(coinId)


        assert(coinDetail.id == coinId)
        assert(coinDetail.coingeckoRank ==1)
        assert(coinDetail.marketCapRank ==1)
        assert(coinDetail.symbol == "btc")
        assert(coinDetail.name == "Bitcoin")
        assertNotNull(coinDetail.description)
        assertNotNull(coinDetail.lastUpdated)
        assertNotNull(coinDetail.lastUpdated)
        assertNotNull(coinDetail.image?.thumb)
        assertNotNull(coinDetail.image?.small)
        assertNotNull(coinDetail.image?.large)
        assertNotNull(coinDetail.blockTimeInMinutes)
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
        val response = getDefaultHeader(mockResIdFile)
        return mapOf(requestUrl to response)
    }

    private fun getResponseMapForCoinIds(mockResIdFile: Int): Map<String, MockResponse> {

        val hostName = endPoints.getHostName()

        // This is a get request, so be sure to include the query string
        val request = StringBuilder()
        request.append(hostName)
        request.append(endPoints.getAllCoinsIdListPath())
        request.append("?", "include_platform", "=", false.toString())


        val requestUrl = request.toString()
        val response = getDefaultHeader(mockResIdFile)
        return mapOf(requestUrl to response)
    }

    private fun getResponseForCoinDetail(mockResIdFile: Int, coinId: String): Map<String, MockResponse> {

        val hostName = endPoints.getHostName()

        // This is a get request, so be sure to include the query string
        val request = StringBuilder()
        request.append(hostName)
        request.append(endPoints.getCoinDetail(), coinId)
        request.append("?", "localization", "=", false.toString())
        request.append("&", "tickers", "=", false.toString())
        request.append("&", "market_data", "=", false.toString())
        request.append("&", "community_data", "=", false.toString())
        request.append("&", "developer_data", "=", false.toString())
        request.append("&", "sparkline", "=", false.toString())

        val requestUrl = request.toString()
        val response = getDefaultHeader(mockResIdFile)
        return mapOf(requestUrl to response)
    }

    private fun getDefaultHeader(mockResIdFile: Int): MockResponse {
        val response = getMockedResponse(OK, mockResIdFile)
        // also mock critical RESPONSE headers!
        response.addHeader("content-type", "application/json; charset=utf-8") // critical!
        return response
    }
}