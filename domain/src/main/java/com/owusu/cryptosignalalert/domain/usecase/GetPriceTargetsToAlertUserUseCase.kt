package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class GetPriceTargetsToAlertUserUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): SuspendedUseCaseUnit<List<PriceTargetDomain>> {

    // needs to return a flow since we will receiving constant updates
    override suspend fun invoke(): List<PriceTargetDomain> {
        return priceTargetsRepository.getPriceTargetsToAlertUser()
    }
}