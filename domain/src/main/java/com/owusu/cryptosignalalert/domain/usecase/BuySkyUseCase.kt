package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.domain.repository.BillingRepository

class BuySkyUseCase(private val billingRepository: BillingRepository): SuspendedUseCase<BuySkyUseCase.Params, Unit> {

    override suspend fun invoke(params: Params) {
        billingRepository.buySku(params.screenProxy, params.sku)
    }

    data class Params(val screenProxy: ScreenProxy, val sku: String)
}