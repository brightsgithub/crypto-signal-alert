package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class PriceTargetsRepositoryImpl : PriceTargetsRepository {
    override suspend fun getPriceTargets(): List<PriceTargetDomain> {
        TODO()
    }
}