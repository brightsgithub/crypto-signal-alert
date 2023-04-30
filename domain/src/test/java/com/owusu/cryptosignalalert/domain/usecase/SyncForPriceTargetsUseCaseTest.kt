package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncForPriceTargetsUseCaseTest {

    private val mockGetPriceTargetsThatHaveNotBeenHitUseCase = mockk<GetPriceTargetsThatHaveNotBeenHitUseCase>()
    private val mockGetCoinsListUseCase = mockk<GetCoinsListUseCase>()
    private val mockUpdatePriceTargetsUseCase = mockk<UpdatePriceTargetsUseCase>()
    private val mockMergeOldPriceTargetWithNewDataUseCase = mockk<MergeOldPriceTargetWithNewDataUseCase>()
    private val mockDateUtil = mockk<CryptoDateUtils>()

    private lateinit var cut: SyncForPriceTargetsUseCase

    @Before
    fun setUp() {
        cut = SyncForPriceTargetsUseCase(
            getPriceTargetsThatHaveNotBeenHitUseCase = mockGetPriceTargetsThatHaveNotBeenHitUseCase,
            getCoinsListUseCase = mockGetCoinsListUseCase,
            updatePriceTargetsUseCase = mockUpdatePriceTargetsUseCase,
            dateUtils = mockDateUtil,
            mergeOldPriceTargetWithNewDataUseCase = mockMergeOldPriceTargetWithNewDataUseCase
        )

        every { mockDateUtil.convertDateToFormattedStringWithTime(any()) } returns "01/01/2020"
        coEvery { mockUpdatePriceTargetsUseCase.invoke(any()) } just runs
        coEvery { mockMergeOldPriceTargetWithNewDataUseCase.invoke(any()) } returns getMockedPriceTargetsDomain()
    }

    @Test
    fun `when there are no price targets return false`() {
        runTest {

            coEvery { mockGetPriceTargetsThatHaveNotBeenHitUseCase.invoke() } returns flowOf(emptyList())

            val wasUpdatePerformed = cut.invoke()

            assert(!wasUpdatePerformed)
        }
    }

    @Test
    fun `calling sync price usecase should return true when a merge has taken place`() {
        runTest {

            // Hierarchical mocking
            val listOfMockedPriceTargets = getMockedPriceTargetsDomain()

            coEvery { mockGetPriceTargetsThatHaveNotBeenHitUseCase.invoke() } returns flowOf(listOfMockedPriceTargets)

            val ids = "bitcoin,ethereum"

            val mockListOfLatestCoins2 =  listOf(CoinDomain(id = "bitcoin", currentPrice = 19000.0),
                CoinDomain(id = "ethereum", currentPrice = 2100.0))

            val params = GetCoinsListUseCase.Params(ids = ids, recordsPerPage = listOfMockedPriceTargets.size)
            coEvery { mockGetCoinsListUseCase.invoke(params) } returns mockListOfLatestCoins2

            val wasUpdatePerformed = cut.invoke()

            assert(wasUpdatePerformed)
        }
    }

    private fun getMockedPriceTargetsDomain(): List<PriceTargetDomain> {
        return listOf<PriceTargetDomain>(
            mockk {
                every { id } returns "bitcoin"
                every { name } returns "Bitcoin"
                every { currentPrice } returns 21518.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 20000.0
                every { hasPriceTargetBeenHit } returns false
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.BELOW
            },
            mockk {
                every { id } returns "ethereum"
                every { name } returns "Ethereum"
                every { currentPrice } returns 1500.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 2000.0
                every { hasPriceTargetBeenHit } returns false
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.ABOVE
            },
        )
    }
}