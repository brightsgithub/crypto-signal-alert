package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.any
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileReader
import java.util.Calendar

class MergeOldPriceTargetWithNewDataUseCaseTest {

    private lateinit var cut: MergeOldPriceTargetWithNewDataUseCase
    private val mockGetHistoricalPriceUseCase  = mockk<GetHistoricalPriceUseCase>()

    @Before
    fun setUp() {
        coEvery {mockGetHistoricalPriceUseCase.invoke(any()) } returns getStaticHistoricalPriceData()
        cut = MergeOldPriceTargetWithNewDataUseCase(mockGetHistoricalPriceUseCase)
    }

    @Test
    fun `completedOnDate should equal lastUpdated when new price target meets the users price target`() {
        runTest {

            val currentPriceTargets = getMockedCurrentPriceTargetsDomain()

            val mockListOfLatestCoins =  listOf(
                CoinDomain(id = "bitcoin", currentPrice = 19000.0),
                CoinDomain(id = "ethereum", currentPrice = 2100.0)
            )

            val params = MergeOldPriceTargetWithNewDataUseCase.Params(
                coinsList = mockListOfLatestCoins,
                priceTargets = currentPriceTargets,
                lastUpdated = "01/01/2022"
            )
            val newlyMergedPriceTargets = cut.invoke(params)

            assert(newlyMergedPriceTargets[0].hasPriceTargetBeenHit)
            assert(newlyMergedPriceTargets[1].hasPriceTargetBeenHit)
            assert(newlyMergedPriceTargets[1].completedOnDate == newlyMergedPriceTargets[1].lastUpdated)
        }
    }

    @Test
    fun  `completedOnDate should equal null when new price target does not meet the users price target`() {
        runTest {

            coEvery {mockGetHistoricalPriceUseCase.invoke(any()) } returnsMany listOf(
                getStaticHistoricalPriceDataHigherThan20k(),
                getStaticHistoricalPriceDataForEthLowerThan2k()
            )

            val currentPriceTargets = getMockedCurrentPriceTargetsDomain()

            val mockListOfLatestCoins =  listOf(
                CoinDomain(id = "bitcoin", currentPrice = 30000.0),
                CoinDomain(id = "ethereum", currentPrice = 500.0)
            )

            val params = MergeOldPriceTargetWithNewDataUseCase.Params(
                coinsList = mockListOfLatestCoins,
                priceTargets = currentPriceTargets,
                lastUpdated = "01/01/2022"
            )
            val newlyMergedPriceTargets = cut.invoke(params)

            assert(!newlyMergedPriceTargets[0].hasPriceTargetBeenHit)
            assert(!newlyMergedPriceTargets[1].hasPriceTargetBeenHit)
            assert(newlyMergedPriceTargets[1].completedOnDate == null)
        }
    }

    @Test
    fun `hasTargetBeenHit should return true when new price target meets the users price target`() {
        runTest {

            val currentPriceTargets = getMockedCurrentPriceTargetsDomain()

            val mockListOfLatestCoins =  listOf(
                CoinDomain(id = "bitcoin", currentPrice = 19000.0),
                CoinDomain(id = "ethereum", currentPrice = 2100.0)
            )

            val params = MergeOldPriceTargetWithNewDataUseCase.Params(
                coinsList = mockListOfLatestCoins,
                priceTargets = currentPriceTargets,
                lastUpdated = "01/01/2022"
            )
            val newlyMergedPriceTargets = cut.invoke(params)

            assert(newlyMergedPriceTargets[0].hasPriceTargetBeenHit)
            assert(newlyMergedPriceTargets[1].hasPriceTargetBeenHit)
        }
    }

    @Test
    fun `hasTargetBeenHit should return false when new price target does not meet users price target`() {
        runTest {
            coEvery {mockGetHistoricalPriceUseCase.invoke(any()) } returnsMany listOf(
                getStaticHistoricalPriceDataHigherThan20k(),
                getStaticHistoricalPriceDataForEthLowerThan2k()
            )

            val currentPriceTargets = getMockedCurrentPriceTargetsDomain()

            val mockListOfLatestCoins =  listOf(
                CoinDomain(id = "bitcoin", currentPrice = 30000.0),
                CoinDomain(id = "ethereum", currentPrice = 500.0)
            )

            val params = MergeOldPriceTargetWithNewDataUseCase.Params(
                coinsList = mockListOfLatestCoins,
                priceTargets = currentPriceTargets,
                lastUpdated = "01/01/2022"
            )
            val newlyMergedPriceTargets = cut.invoke(params)

            assert(!newlyMergedPriceTargets[0].hasPriceTargetBeenHit)
            assert(!newlyMergedPriceTargets[1].hasPriceTargetBeenHit)
        }
    }

    @Test
    fun `hasTargetBeenHit should not be updated to false if previously true`() {
        runTest {

            val currentPriceTargets = getMockedPriceTargetsAlreadyHit()

            val mockListOfLatestCoins =  listOf(
                CoinDomain(id = "bitcoin", currentPrice = 30000.0),
                CoinDomain(id = "ethereum", currentPrice = 500.0)
            )

            val params = MergeOldPriceTargetWithNewDataUseCase.Params(
                coinsList = mockListOfLatestCoins,
                priceTargets = currentPriceTargets,
                lastUpdated = "01/01/2022"
            )
            val newlyMergedPriceTargets = cut.invoke(params)

            // We have previously met our price target, but on this sync
            // the new prices have not, but we don't care. We wont change it since hasPriceTargetBeenHit
            // was previously true and the user is about to be notified
            // (when hasUserBeenAlerted will eventually be set to true during send notification process.
            // If notification fails at least on the next sync hasPriceTargetBeenHit will remain true
            // so attempting to send notification can be retried)
            // or hasUserBeenAlerted is already true which means the user has been notified
            assert(newlyMergedPriceTargets[0].hasPriceTargetBeenHit)
            assert(newlyMergedPriceTargets[1].hasPriceTargetBeenHit)
        }
    }

    @Test
    fun `completedOnDate should return the same completedOnDate if hasPriceTargetBeenHit has been previously true`() {
        runTest {

            val currentPriceTargets = getMockedPriceTargetsAlreadyHitWithCompletedBy()

            val mockListOfLatestCoins =  listOf(
                CoinDomain(id = "bitcoin", currentPrice = 30000.0),
                CoinDomain(id = "ethereum", currentPrice = 500.0)
            )

            val params = MergeOldPriceTargetWithNewDataUseCase.Params(
                coinsList = mockListOfLatestCoins,
                priceTargets = currentPriceTargets,
                lastUpdated = "01/01/2022"
            )
            val newlyMergedPriceTargets = cut.invoke(params)

            // We have previously met our price target, but on this sync
            // the new prices have not, but we don't care. We wont change it since hasPriceTargetBeenHit
            // was previously true and the user is about to be notified
            // (when hasUserBeenAlerted will eventually be set to true during send notification process.
            // If notification fails at least on the next sync hasPriceTargetBeenHit will remain true
            // so attempting to send notification can be retried)
            // or hasUserBeenAlerted is already true which means the user has been notified
            assert(newlyMergedPriceTargets[0].hasPriceTargetBeenHit)
            assert(newlyMergedPriceTargets[1].hasPriceTargetBeenHit)
            assert(newlyMergedPriceTargets[1].completedOnDate == currentPriceTargets[1].completedOnDate)
        }
    }

    // Hierarchical mocking
    private fun getMockedCurrentPriceTargetsDomain(): List<PriceTargetDomain> {
        return listOf<PriceTargetDomain>(
            mockk {
                every { localPrimeId } returns 0
                every { id } returns "bitcoin"
                every { name } returns "Bitcoin"
                every { currentPrice } returns 21518.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 20000.0
                every { hasPriceTargetBeenHit } returns false
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.BELOW
                every { completedOnDate } returns null
            },
            mockk {
                every { localPrimeId } returns 1
                every { id } returns "ethereum"
                every { name } returns "Ethereum"
                every { currentPrice } returns 1500.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 2000.0
                every { hasPriceTargetBeenHit } returns false
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.ABOVE
                every { completedOnDate } returns null
            },
        )
    }

    private fun getMockedPriceTargetsAlreadyHit(): List<PriceTargetDomain> {
        return listOf<PriceTargetDomain>(
            mockk {
                every { localPrimeId } returns 0
                every { id } returns "bitcoin"
                every { name } returns "Bitcoin"
                every { currentPrice } returns 21518.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 20000.0
                every { hasPriceTargetBeenHit } returns true
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.BELOW
                every { completedOnDate } returns null
            },
            mockk {
                every { localPrimeId } returns 1
                every { id } returns "ethereum"
                every { name } returns "Ethereum"
                every { currentPrice } returns 1500.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 2000.0
                every { hasPriceTargetBeenHit } returns true
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.ABOVE
                every { completedOnDate } returns null
            },
        )
    }

    private fun getMockedPriceTargetsAlreadyHitWithCompletedBy(): List<PriceTargetDomain> {
        return listOf<PriceTargetDomain>(
            mockk {
                every { localPrimeId } returns 0
                every { id } returns "bitcoin"
                every { name } returns "Bitcoin"
                every { currentPrice } returns 21518.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 20000.0
                every { hasPriceTargetBeenHit } returns true
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.BELOW
                every { completedOnDate } returns "Jan 1"
            },
            mockk {
                every { localPrimeId } returns 1
                every { id } returns "ethereum"
                every { name } returns "Ethereum"
                every { currentPrice } returns 1500.0
                every { lastUpdated } returns "2022-07-09T12:31:40.339Z"
                every { userPriceTarget } returns 2000.0
                every { hasPriceTargetBeenHit } returns true
                every { hasUserBeenAlerted } returns false
                every { priceTargetDirection } returns PriceTargetDirection.ABOVE
                every { completedOnDate } returns "Jan 1"
            },
        )
    }

    private fun getStaticHistoricalPriceData(): HistoricPriceWrapperDomain {
        val prices = arrayListOf<HistoricPriceDomain>()
        prices.add(HistoricPriceDomain(1678892579000, 24808.153320502854))
        prices.add(HistoricPriceDomain(1678892139910, 24750.833170525402))
        prices.add(HistoricPriceDomain(1678891806700, 24608.64962541245))
        prices.add(HistoricPriceDomain(1678891498363, 24651.314584738193))
        prices.add(HistoricPriceDomain(1678891220459, 24751.780149332506))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(30), 24751.104247902713))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(25), 24872.82892089749))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(20), 24973.988273159932))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(15), 24969.360546799628))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(10), 19000.579825437104))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(5), 24982.33290077767))
        return HistoricPriceWrapperDomain(prices)
    }

    private fun getStaticHistoricalPriceDataHigherThan20k(): HistoricPriceWrapperDomain {
        val prices = arrayListOf<HistoricPriceDomain>()
        prices.add(HistoricPriceDomain(1678892579000, 24808.153320502854))
        prices.add(HistoricPriceDomain(1678892139910, 24750.833170525402))
        prices.add(HistoricPriceDomain(1678891806700, 24608.64962541245))
        prices.add(HistoricPriceDomain(1678891498363, 24651.314584738193))
        prices.add(HistoricPriceDomain(1678891220459, 24751.780149332506))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(30), 24751.104247902713))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(25), 24872.82892089749))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(20), 24973.988273159932))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(15), 24969.360546799628))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(10), 25969.579825437104))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(5), 24982.33290077767))
        return HistoricPriceWrapperDomain(prices)
    }

    private fun getStaticHistoricalPriceDataForEthLowerThan2k(): HistoricPriceWrapperDomain {
        val prices = arrayListOf<HistoricPriceDomain>()
        prices.add(HistoricPriceDomain(1678892579000, 1000.153320502854))
        prices.add(HistoricPriceDomain(1678892139910, 1000.833170525402))
        prices.add(HistoricPriceDomain(1678891806700, 1000.64962541245))
        prices.add(HistoricPriceDomain(1678891498363, 1000.314584738193))
        prices.add(HistoricPriceDomain(1678891220459, 1000.780149332506))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(30), 1000.104247902713))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(25), 1000.82892089749))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(20), 1000.988273159932))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(15), 1000.360546799628))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(10), 1000.579825437104))
        prices.add(HistoricPriceDomain(getHistoricalTimestamp(5), 1000.33290077767))
        return HistoricPriceWrapperDomain(prices)
    }

    private fun getHistoricalTimestamp(timeInMinutesToSubtract: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -timeInMinutesToSubtract)
        return calendar.timeInMillis
    }
}