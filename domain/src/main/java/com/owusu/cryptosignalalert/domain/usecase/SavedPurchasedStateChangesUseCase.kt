package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.states.PurchasedStateDomain
import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow

class SavedPurchasedStateChangesUseCase(
    private val appPreferencesRepository: AppPreferencesRepository
    ):UseCaseUnit<Flow<PurchasedStateDomain>> {
    override fun invoke(): Flow<PurchasedStateDomain> {
        return appPreferencesRepository.listenForPurchasedDataState()
    }
}