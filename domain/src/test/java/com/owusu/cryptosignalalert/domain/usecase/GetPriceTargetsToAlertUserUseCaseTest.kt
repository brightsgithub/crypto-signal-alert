package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPriceTargetsToAlertUserUseCaseTest {

    private val mockPriceTargetsRepository = mockk<PriceTargetsRepository>()
    private lateinit var cut: GetPriceTargetsToAlertUserUseCase

    @Before
    fun setUp() {
        cut = GetPriceTargetsToAlertUserUseCase(mockPriceTargetsRepository)
    }

    @Test
    fun `should call getPriceTargetToAlertUser on repo`() {
        runTest {

            coEvery { mockPriceTargetsRepository.getPriceTargetsToAlertUser() } returns mockk()

            cut.invoke()

            coVerify { mockPriceTargetsRepository.getPriceTargetsToAlertUser() }
        }
    }
}