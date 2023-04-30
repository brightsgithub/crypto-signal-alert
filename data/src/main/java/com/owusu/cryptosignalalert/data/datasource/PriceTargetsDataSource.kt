package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import kotlinx.coroutines.flow.Flow

interface PriceTargetsDataSource {
    suspend fun getPriceTargets(): List<PriceTargetDomain>
    suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain>
    fun getPriceTargetsThatHaveNotBeenHit(): Flow<List<PriceTargetDomain>>
    suspend fun insertPriceTargets(priceTargets: List<PriceTargetDomain>): Boolean
    suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun getPriceTargetsCount(): Int
    suspend fun getPriceTargetsThatHaveNotBeenHitCount(): Int
    suspend fun nukeAll()
}