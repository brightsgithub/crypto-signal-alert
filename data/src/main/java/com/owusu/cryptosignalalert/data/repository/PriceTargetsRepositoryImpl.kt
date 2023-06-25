package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.PriceTargetsDataSource
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.states.UpdateSyncState
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class PriceTargetsRepositoryImpl(
    private val priceTargetsDataSource: PriceTargetsDataSource
) : PriceTargetsRepository {

    private val _syncStateUpdates = MutableStateFlow(UpdateSyncState())
    private val syncStateUpdates :Flow<UpdateSyncState> = _syncStateUpdates

    override fun listenToSyncUpdateState(): Flow<UpdateSyncState> {
        return syncStateUpdates
    }

    override suspend fun updateSyncState(updateSyncState: UpdateSyncState) {
        _syncStateUpdates.value = _syncStateUpdates.value.copy(remainingPercentageOfWorkToBeDone = updateSyncState.remainingPercentageOfWorkToBeDone)
    }

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