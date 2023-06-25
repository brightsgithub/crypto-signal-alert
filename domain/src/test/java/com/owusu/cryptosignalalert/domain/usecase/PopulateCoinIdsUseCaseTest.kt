package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinIdDomain
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class PopulateCoinIdsUseCaseTest {
    private lateinit var coinsRepository: CoinsRepository
    private lateinit var useCase: PopulateCoinIdsUseCase

    @Before
    fun setup() {
        coinsRepository = mockk(relaxed = true)
        useCase = PopulateCoinIdsUseCase(coinsRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test invoke - current time is not greater than last updated time`() = runBlocking {
        // Arrange
        val currentTime = Calendar.getInstance()
        currentTime.timeInMillis = 500L
        val lastCoinIdUpdate = 1000L
        every { coinsRepository.getLastCoinIdUpdate() } returns lastCoinIdUpdate

        // Act
        useCase.invoke(PopulateCoinIdsUseCase.Params(currentTime = currentTime))

        // Assert
        verify { coinsRepository.getLastCoinIdUpdate() }
        coVerify(exactly = 0) { coinsRepository.getAllCoinIds() }
        verify(exactly = 0) { coinsRepository.setLastCoinIdUpdate(any()) }
        coVerify(exactly = 0) { coinsRepository.saveAllCoinIds(any()) }
    }

    @Test
    fun `last updated has not been set`() = runBlocking {
        val coinIds = listOf(
            CoinIdDomain("bitcoin", "Bitcoin", "btc"),
            CoinIdDomain("ethereum", "Ethereum", "eth")
        )
        val currentTime = Calendar.getInstance()
        currentTime.timeInMillis = 2000L
        val lastCoinIdUpdate = 0L
        every { coinsRepository.getLastCoinIdUpdate() } returns lastCoinIdUpdate
        coEvery { coinsRepository.getAllCoinIds() } returns coinIds

        // Act
        useCase.invoke(PopulateCoinIdsUseCase.Params(currentTime = currentTime))

        // Assert
        verify { coinsRepository.getLastCoinIdUpdate() }
        coVerify { coinsRepository.getAllCoinIds() }
        coVerify { coinsRepository.saveAllCoinIds(coinIds) }
        verify { coinsRepository.setLastCoinIdUpdate(any()) }
        confirmVerified(coinsRepository)
    }

    @Test
    fun `test invoke - current time is greater than last updated time`() = runBlocking {
        // Arrange
        val coinIds = listOf(
            CoinIdDomain("bitcoin", "Bitcoin", "btc"),
            CoinIdDomain("ethereum", "Ethereum", "eth")
        )
        val currentTime = Calendar.getInstance()
        currentTime.timeInMillis = 2000L
        val lastCoinIdUpdate = 1000L
        every { coinsRepository.getLastCoinIdUpdate() } returns lastCoinIdUpdate
        coEvery { coinsRepository.getAllCoinIds() } returns coinIds

        // Act
        useCase.invoke(PopulateCoinIdsUseCase.Params(currentTime = currentTime))

        // Assert
        verify { coinsRepository.getLastCoinIdUpdate() }
        coVerify { coinsRepository.getAllCoinIds() }
        coVerify { coinsRepository.saveAllCoinIds(coinIds) }
        verify { coinsRepository.setLastCoinIdUpdate(any()) }
        confirmVerified(coinsRepository)
    }
}

