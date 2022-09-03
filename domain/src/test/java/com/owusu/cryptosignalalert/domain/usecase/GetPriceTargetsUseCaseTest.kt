package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPriceTargetsUseCaseTest {

    private val mockPriceTargetsRepository = mockk<PriceTargetsRepository>()
    private lateinit var cut: GetPriceTargetsUseCase

    @Before
    fun setUp() {
        cut = GetPriceTargetsUseCase(mockPriceTargetsRepository)
    }

    @Test
    fun `should call getPriceTargets on repo`() {
        runTest {

            coEvery { mockPriceTargetsRepository.getPriceTargets() } returns mockk()

            cut.invoke()

            coVerify { mockPriceTargetsRepository.getPriceTargets() }
        }
    }
}