package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class DeletePriceTargetsUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): SuspendedUseCase<DeletePriceTargetsUseCase.Params, Unit> {
    override suspend fun invoke(params: Params) {
        priceTargetsRepository.deletePriceTargets(params.priceTargets)
    }
    data class Params(val priceTargets: List<PriceTargetDomain>)
}