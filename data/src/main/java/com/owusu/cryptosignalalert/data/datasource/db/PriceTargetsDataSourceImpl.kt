package com.owusu.cryptosignalalert.data.datasource.db

import com.owusu.cryptosignalalert.data.datasource.PriceTargetsDataSource
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class PriceTargetsDataSourceImpl(
    private val priceTargetDao: PriceTargetDao,
    private val mapper: DataAPIListMapper<PriceTargetEntity, PriceTargetDomain>
    ): PriceTargetsDataSource {

    @Synchronized
    override fun getPriceTargets(): Flow<List<PriceTargetDomain>> {
        return priceTargetDao.getPriceTargets().map {it
            mapper.transform(it)
        }
    }

    @Synchronized
    override suspend fun getPriceTargetsCount(): Int {
        return priceTargetDao.getPriceTargetsCount()
    }

    override suspend fun getPriceTargetsThatHaveNotBeenHitCount(): Int {
        return priceTargetDao.getPriceTargetsThatHaveNotBeenHitCount()
    }

    override suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain> {
        val entityList = priceTargetDao.getPriceTargetsToAlertUser()
        return mapper.transform(entityList)
    }

    override fun getPriceTargetsThatHaveNotBeenHit(): Flow<List<PriceTargetDomain>> {
        return priceTargetDao.getPriceTargetsThatHaveNotBeenHit().map {
            mapper.transform(it)
        }
    }

    override suspend fun insertPriceTargets(priceTargets: List<PriceTargetDomain>): Boolean {
        val entityList = mapper.reverseTransformation(priceTargets)
        val rowIds = priceTargetDao.insertPriceTargets(entityList)
        return rowIds.isNotEmpty()
    }

    override suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>) {
        val entityList = mapper.reverseTransformation(priceTargets)
        priceTargetDao.updatePriceTargets(entityList)
    }

    override suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>) {
        val entityList = mapper.reverseTransformation(priceTargets)
        priceTargetDao.deletePriceTargets(entityList)
    }

    override suspend fun nukeAll() {
        priceTargetDao.nukeTable()
    }
}