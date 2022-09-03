package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class UpdatePriceTargetsUseCaseTest {

    private val mockPriceTargetsRepository = mockk<PriceTargetsRepository>()

    private lateinit var cut: UpdatePriceTargetsUseCase

    @Before
    fun setUp() {
        cut = UpdatePriceTargetsUseCase(mockPriceTargetsRepository)
    }

    @Test
    fun update_price_targets_should_call_update_on_repo() {
        runTest {

            coEvery { mockPriceTargetsRepository.updatePriceTargets(any()) } just runs

            val listOfPriceTargets = mockk<List<PriceTargetDomain>>()

            cut.invoke(UpdatePriceTargetsUseCase.Params(listOfPriceTargets))

            coVerify { mockPriceTargetsRepository.updatePriceTargets(any()) }
        }
    }
}