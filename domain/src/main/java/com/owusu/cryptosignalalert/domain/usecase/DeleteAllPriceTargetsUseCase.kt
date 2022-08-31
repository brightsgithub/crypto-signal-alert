package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class DeleteAllPriceTargetsUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): SuspendedUseCaseUnit<Unit> {
    override suspend fun invoke() {
        priceTargetsRepository.nukeAll()
    }
}