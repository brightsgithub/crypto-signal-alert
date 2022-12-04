package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.BillingReadyState
import com.owusu.cryptosignalalert.domain.models.states.NewPurchasesState
import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    fun observeBillingReadyStateFlow(): Flow<BillingReadyState>
    suspend fun getSkuDetails(skuList: List<String>? = null): List<SkuDetailsDomain>
    fun buySku(screenProxy: ScreenProxy, sku: String)
    val billingFlowInProcess: Flow<Boolean>
}