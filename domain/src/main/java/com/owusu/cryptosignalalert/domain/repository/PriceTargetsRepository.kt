package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import kotlinx.coroutines.flow.Flow

interface PriceTargetsRepository {
    fun getPriceTargets(): Flow<List<PriceTargetDomain>>
    suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain>
    fun getPriceTargetsThatHaveNotBeenHit(): Flow<List<PriceTargetDomain>>
    suspend fun saveNewPriceTargets(priceTargets: List<PriceTargetDomain>): Boolean
    suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun getPriceTargetsCount(): Int
    suspend fun nukeAll()
    suspend fun getPriceTargetsThatHaveNotBeenHitCount(): Int
}