package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

interface PriceTargetsRepository {
    suspend fun getPriceTargets(): List<PriceTargetDomain>
}