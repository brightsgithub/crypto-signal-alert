package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class GetPriceTargetsThatHaveNotBeenHitUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): SuspendedUseCaseUnit<List<PriceTargetDomain>> {

    override suspend fun invoke(): List<PriceTargetDomain> {
        return priceTargetsRepository.getPriceTargetsThatHaveNotBeenHit()
    }
}