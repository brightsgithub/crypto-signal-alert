package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveNewPriceTargetsUseCaseTest {

    private val mockPriceTargetsRepository = mockk<PriceTargetsRepository>()
    private lateinit var cut: SaveNewPriceTargetsUseCase

    @Before
    fun setUp() {
        cut = SaveNewPriceTargetsUseCase(mockPriceTargetsRepository)
    }

    @Test
    fun `should call saveNewPriceTargets on repo`() {
        runTest {

            val listOfPriceTargets = mockk<List<PriceTargetDomain>>()
            coEvery { mockPriceTargetsRepository.saveNewPriceTargets(listOfPriceTargets) } just runs


            val params = SaveNewPriceTargetsUseCase.Params(listOfPriceTargets)
            cut.invoke(params)

            coVerify { mockPriceTargetsRepository.saveNewPriceTargets(listOfPriceTargets) }
        }
    }
}