package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.states.UpdateSyncState
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import kotlinx.coroutines.flow.Flow

class ListenToSyncPriceTargetsUpdatesUseCase(
    private val priceTargetsRepository: PriceTargetsRepository
): UseCaseUnit<Flow<UpdateSyncState>> {

    override fun invoke(): Flow<UpdateSyncState> {
        return priceTargetsRepository.listenToSyncUpdateState()
    }
}