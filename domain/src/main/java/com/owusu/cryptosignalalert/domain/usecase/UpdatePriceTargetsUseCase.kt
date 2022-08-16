package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class UpdatePriceTargetsUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): SuspendedUseCase<UpdatePriceTargetsUseCase.Params, Unit> {

    override suspend fun invoke(params: Params) {
        priceTargetsRepository.updatePriceTargets(params.priceTargets)
    }

    data class Params(val priceTargets: List<PriceTargetDomain>)
}