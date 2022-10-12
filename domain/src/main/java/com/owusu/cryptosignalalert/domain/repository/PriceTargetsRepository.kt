package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

interface PriceTargetsRepository {
    suspend fun getPriceTargets(): List<PriceTargetDomain>
    suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain>
    suspend fun getPriceTargetsThatHaveNotBeenHit(): List<PriceTargetDomain>
    suspend fun saveNewPriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun nukeAll()
}