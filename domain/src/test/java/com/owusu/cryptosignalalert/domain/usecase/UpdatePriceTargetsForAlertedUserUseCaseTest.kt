package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdatePriceTargetsForAlertedUserUseCaseTest {

    private val mockUpdatePriceTargetsUseCase = mockk<UpdatePriceTargetsUseCase>()

    private lateinit var cut: UpdatePriceTargetsForAlertedUserUseCase

    @Before
    fun setUp() {
        cut = UpdatePriceTargetsForAlertedUserUseCase(mockUpdatePriceTargetsUseCase)
    }

    @Test
    fun `should_call_UpdatePriceTargetsUseCase_when_hasUserBeenAlerted is true`() {
        runTest {

            coEvery { mockUpdatePriceTargetsUseCase.invoke(any()) } just runs

            val listOfPriceTargets = getPriceTargetsAlreadyHit()

            cut.invoke(UpdatePriceTargetsForAlertedUserUseCase.Params(true, listOfPriceTargets))

            coVerify { mockUpdatePriceTargetsUseCase.invoke(any()) }
        }
    }

    @Test
    fun `should_call_not call UpdatePriceTargetsUseCase_when_hasUserBeenAlerted is false`() {
        runTest {

            val listOfPriceTargets = getPriceTargetsAlreadyHit()

            cut.invoke(UpdatePriceTargetsForAlertedUserUseCase.Params(false, listOfPriceTargets))

            coVerify(exactly = 0) { mockUpdatePriceTargetsUseCase.invoke(any()) }
        }
    }

    private fun getPriceTargetsAlreadyHit(): List<PriceTargetDomain> {
        return listOf(
            PriceTargetDomain(id = "bitcoin", hasUserBeenAlerted = true),
            PriceTargetDomain(id = "ethereum", hasUserBeenAlerted = true)
        )
    }
}
