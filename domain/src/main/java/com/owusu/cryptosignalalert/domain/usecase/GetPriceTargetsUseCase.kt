package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetsWrapper
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import kotlinx.coroutines.flow.Flow

class GetPriceTargetsUseCase(
    private val priceTargetsRepository: PriceTargetsRepository):
    UseCaseUnit<Flow<List<PriceTargetDomain>>> {

    // needs to return a flow since we will receiving constant updates
    override fun invoke(): Flow<List<PriceTargetDomain>> {
        return priceTargetsRepository.getPriceTargets()
    }
}