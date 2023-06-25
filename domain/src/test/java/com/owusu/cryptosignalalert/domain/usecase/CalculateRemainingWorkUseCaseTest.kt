package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.usecase.CalculateRemainingWorkUseCase.Params
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateRemainingWorkUseCaseTest {

    @Test
    fun `test calculateRemainingPercentageOfWorkDone`() {
        // Arrange
        val totalPriceTargetsSize = 10
        val numberOfItemsCompleted = 3
        val expectedRemainingPercentage = 0.7f

        val useCase = CalculateRemainingWorkUseCase()

        // Act
        val result = useCase.invoke(Params(totalPriceTargetsSize, numberOfItemsCompleted))

        // Assert
        assertEquals(expectedRemainingPercentage, result)
    }

    @Test
    fun `test calculateRemainingPercentageOfWorkDone2`() {
        // Arrange
        val totalPriceTargetsSize = 10
        val numberOfItemsCompleted = 8
        val expectedRemainingPercentage = 0.2f

        val useCase = CalculateRemainingWorkUseCase()

        // Act
        val result = useCase.invoke(Params(totalPriceTargetsSize, numberOfItemsCompleted))

        // Assert
        assertEquals(expectedRemainingPercentage, result)
    }

    @Test
    fun `test calculateRemainingPercentageOfWorkDone3`() {
        // Arrange
        val totalPriceTargetsSize = 10
        val numberOfItemsCompleted = 10
        val expectedRemainingPercentage = 0f

        val useCase = CalculateRemainingWorkUseCase()

        // Act
        val result = useCase.invoke(Params(totalPriceTargetsSize, numberOfItemsCompleted))

        // Assert
        assertEquals(expectedRemainingPercentage, result)
    }

    @Test
    fun `test calculateRemainingPercentageOfWorkDone4`() {
        // Arrange
        val totalPriceTargetsSize = 10
        val numberOfItemsCompleted = 2
        val expectedRemainingPercentage = 0.8f

        val useCase = CalculateRemainingWorkUseCase()

        // Act
        val result = useCase.invoke(Params(totalPriceTargetsSize, numberOfItemsCompleted))

        // Assert
        assertEquals(expectedRemainingPercentage, result)
    }

    @Test
    fun `test calculateRemainingPercentageOfWorkDone5`() {
        // Arrange
        val totalPriceTargetsSize = 10
        val numberOfItemsCompleted = 0
        val expectedRemainingPercentage = 1f

        val useCase = CalculateRemainingWorkUseCase()

        // Act
        val result = useCase.invoke(Params(totalPriceTargetsSize, numberOfItemsCompleted))

        // Assert
        assertEquals(expectedRemainingPercentage, result)
    }

    @Test
    fun `test calculateRemainingPercentageOfWorkDone6`() {
        // Arrange
        val totalPriceTargetsSize = 0
        val numberOfItemsCompleted = 0
        val expectedRemainingPercentage = 1f

        val useCase = CalculateRemainingWorkUseCase()

        // Act
        val result = useCase.invoke(Params(totalPriceTargetsSize, numberOfItemsCompleted))

        // Assert
        assertEquals(expectedRemainingPercentage, result)
    }
}
