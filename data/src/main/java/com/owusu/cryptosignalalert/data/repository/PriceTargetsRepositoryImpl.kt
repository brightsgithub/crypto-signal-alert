package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.PriceTargetsDataSource
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class PriceTargetsRepositoryImpl(
    private val priceTargetsDataSource: PriceTargetsDataSource
) : PriceTargetsRepository {

    override suspend fun getPriceTargets(): List<PriceTargetDomain> {
        return priceTargetsDataSource.getPriceTargets()
    }

    override suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain> {
        return priceTargetsDataSource.getPriceTargetsToAlertUser()
    }

    override suspend fun saveNewPriceTargets(priceTargets: List<PriceTargetDomain>) {
        priceTargetsDataSource.insertPriceTargets(priceTargets)
    }

    override suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>) {
        priceTargetsDataSource.updatePriceTargets(priceTargets)
    }

    override suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>) {
        priceTargetsDataSource.deletePriceTargets(priceTargets)
    }

    override suspend fun nukeAll() {
        priceTargetsDataSource.nukeAll()
    }
}