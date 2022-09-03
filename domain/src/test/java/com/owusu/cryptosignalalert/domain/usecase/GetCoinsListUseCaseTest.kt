package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCoinsListUseCaseTest {

    private val mockCoinsRepository = mockk<CoinsRepository>()
    private lateinit var cut: GetCoinsListUseCase

    @Before
    fun setUp() {
        cut = GetCoinsListUseCase(mockCoinsRepository)
    }

    @Test
    fun `should call getCoinsList on repo`() {
        runTest {

            val listOfPriceTargets = mockk<List<PriceTargetDomain>>()
            coEvery { mockCoinsRepository.getCoinsList(any(),any(), any(), any()) } returns mockk()


            val params = GetCoinsListUseCase.Params()
            cut.invoke(params)

            coVerify { mockCoinsRepository.getCoinsList(any(),any(), any(), any()) }
        }
    }
}