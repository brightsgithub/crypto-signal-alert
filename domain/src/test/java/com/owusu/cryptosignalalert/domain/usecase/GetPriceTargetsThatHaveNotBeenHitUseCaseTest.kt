package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPriceTargetsThatHaveNotBeenHitUseCaseTest {

    private val mockPriceTargetsRepository = mockk<PriceTargetsRepository>()
    private lateinit var cut: GetPriceTargetsThatHaveNotBeenHitUseCase

    @Before
    fun setUp() {
        cut = GetPriceTargetsThatHaveNotBeenHitUseCase(mockPriceTargetsRepository)
    }

    @Test
    fun `should call getPriceTargetsThatHaveNotBeenHit on repo`() {
        runTest {

            coEvery { mockPriceTargetsRepository.getPriceTargetsThatHaveNotBeenHit() } returns mockk()

            cut.invoke()

            coVerify { mockPriceTargetsRepository.getPriceTargetsThatHaveNotBeenHit() }
        }
    }
}