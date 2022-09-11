package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MergeOldPriceTargetWithNewDataUseCaseTest {

    private lateinit var cut: MergeOldPriceTargetWithNewDataUseCase

    @Before
    fun setUp() {
        cut = MergeOldPriceTargetWithNewDataUseCase()
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
            },
        )
    }
}