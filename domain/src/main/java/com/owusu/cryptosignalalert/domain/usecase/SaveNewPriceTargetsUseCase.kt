package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class SaveNewPriceTargetsUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): SuspendedUseCase<SaveNewPriceTargetsUseCase.Params, Unit> {
    override suspend fun invoke(params: Params) {
        priceTargetsRepository.saveNewPriceTargets(params.priceTargets)
    }
    data class Params(val priceTargets: List<PriceTargetDomain>)
}

