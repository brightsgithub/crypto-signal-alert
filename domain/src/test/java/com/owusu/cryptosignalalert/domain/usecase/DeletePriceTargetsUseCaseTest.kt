package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeletePriceTargetsUseCaseTest {

    private val mockPriceTargetsRepository = mockk<PriceTargetsRepository>()
    private lateinit var cut: DeletePriceTargetsUseCase

    @Before
    fun setUp() {
        cut = DeletePriceTargetsUseCase(mockPriceTargetsRepository)
    }

    @Test
    fun `should call deletePriceTargets on repo`() {
        runTest {

            val listOfPriceTargets = mockk<List<PriceTargetDomain>>()
            coEvery { mockPriceTargetsRepository.deletePriceTargets(listOfPriceTargets) } just runs


            val params = DeletePriceTargetsUseCase.Params(listOfPriceTargets)
            cut.invoke(params)

            coVerify { mockPriceTargetsRepository.deletePriceTargets(listOfPriceTargets) }
        }
    }
}