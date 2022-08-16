package com.owusu.cryptosignalalert.data.datasource.db

import com.owusu.cryptosignalalert.data.datasource.PriceTargetsDataSource
import com.owusu.cryptosignalalert.data.mappers.DataAPIListMapper
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

class PriceTargetsDataSourceImpl(
    private val priceTargetDao: PriceTargetDao,
    private val mapper: DataAPIListMapper<PriceTargetEntity, PriceTargetDomain>
    ): PriceTargetsDataSource {

    override suspend fun getPriceTargets(): List<PriceTargetDomain> {
        val entityList = priceTargetDao.getPriceTargets()
        return mapper.transform(entityList)
    }

    override suspend fun getPriceTargetsToAlertUser(): List<PriceTargetDomain> {
        val entityList = priceTargetDao.getPriceTargetsToAlertUser()
        return mapper.transform(entityList)
    }

    override suspend fun insertPriceTargets(priceTargets: List<PriceTargetDomain>) {
        val entityList = mapper.reverseTransformation(priceTargets)
        priceTargetDao.insertPriceTargets(entityList)
    }

    override suspend fun updatePriceTargets(priceTargets: List<PriceTargetDomain>) {
        val entityList = mapper.reverseTransformation(priceTargets)
        priceTargetDao.updatePriceTargets(entityList)
    }

    override suspend fun deletePriceTargets(priceTargets: List<PriceTargetDomain>) {
        val entityList = mapper.reverseTransformation(priceTargets)
        priceTargetDao.deletePriceTargets(entityList)
    }
}