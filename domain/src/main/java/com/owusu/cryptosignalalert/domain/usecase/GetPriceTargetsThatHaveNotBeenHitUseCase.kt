package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import kotlinx.coroutines.flow.Flow

class GetPriceTargetsThatHaveNotBeenHitUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): UseCaseUnit<Flow<List<PriceTargetDomain>>> {

    override fun invoke(): Flow<List<PriceTargetDomain>> {
        return priceTargetsRepository.getPriceTargetsThatHaveNotBeenHit()
    }
}