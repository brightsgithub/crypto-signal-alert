package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.BillingRepository

class RefreshSkuDetailsUseCase(
    private val billingRepository: BillingRepository
): SuspendedUseCaseUnit<Unit> {

    override suspend fun invoke() {
        billingRepository.refresh()
    }
}