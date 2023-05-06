package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.PriceTargetsDataSource
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import kotlinx.coroutines.flow.Flow

class PriceTargetsRepositoryImpl(
    private val priceTargetsDataSource: PriceTargetsDataSource
) : PriceTargetsRepository {

    override fun getPriceTargets(): Flow<List<PriceTargetDomain>> {
        return priceTargetsDataSource.getPriceTargets()
    }

    override suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain> {
        return priceTargetsDataSource.getPriceTargetsToAlertUser()
    }

    override fun getPriceTargetsThatHaveNotBeenHit(): Flow<List<PriceTargetDomain>> {
        return priceTargetsDataSource.getPriceTargetsThatHaveNotBeenHit()
    }

    override suspend fun getPriceTargetsThatHaveNotBeenHitCount(): Int {
        return priceTargetsDataSource.getPriceTargetsThatHaveNotBeenHitCount()
    }

    override suspend fun saveNewPriceTargets(priceTargets: List<PriceTargetDomain>): Boolean {
        return priceTargetsDataSource.insertPriceTargets(priceTargets)
    }

    override suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>) {
        priceTargetsDataSource.updatePriceTargets(priceTargets)
    }

    override suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>) {
        priceTargetsDataSource.deletePriceTargets(priceTargets)
    }

    override suspend fun getPriceTargetsCount(): Int {
        return priceTargetsDataSource.getPriceTargetsCount()
    }

    override suspend fun nukeAll() {
        priceTargetsDataSource.nukeAll()
    }
}