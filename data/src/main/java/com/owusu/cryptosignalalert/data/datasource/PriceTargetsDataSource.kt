package com.owusu.cryptosignalalert.data.datasource

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

interface PriceTargetsDataSource {
    suspend fun getPriceTargets(): List<PriceTargetDomain>
    suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain>
    suspend fun insertPriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>)
    suspend fun nukeAll()
}