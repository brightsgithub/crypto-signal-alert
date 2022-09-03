package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteAllPriceTargetsUseCaseTest {

    private val mockPriceTargetsRepository = mockk<PriceTargetsRepository>()

    private lateinit var cut: DeleteAllPriceTargetsUseCase

    @Before
    fun setUp() {
        cut = DeleteAllPriceTargetsUseCase(mockPriceTargetsRepository)
    }

    @Test
    fun `should call nukeAll on Repo`() {
        runTest {
            coEvery { mockPriceTargetsRepository.nukeAll() } just runs
            cut.invoke()
            coVerify { mockPriceTargetsRepository.nukeAll() }
        }
    }
}