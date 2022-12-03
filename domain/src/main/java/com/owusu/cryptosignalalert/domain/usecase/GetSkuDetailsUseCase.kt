package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.states.BillingReadyState
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.repository.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetSkuDetailsUseCase(
    private val billingRepository: BillingRepository):
    UseCase<CoroutineScope, Flow<SkuDetailsState>> {

    private val state = MutableStateFlow<SkuDetailsState>(SkuDetailsState.NotReady)

    override fun invoke(scope: CoroutineScope): Flow<SkuDetailsState> {
        scope.launch {
            billingRepository.observeBillingReadyStateFlow().collect { billingReadyState ->
                delay(5000)
                when(billingReadyState) {
                    is BillingReadyState.NoSkusExist -> state.emit(SkuDetailsState.NoSkusExist)
                    is BillingReadyState.NotReady -> state.emit(SkuDetailsState.NotReady)
                    is BillingReadyState.Ready -> {
                        val result = billingRepository.getSkuDetails()
                        state.emit(SkuDetailsState.Success(result))
                    }
                }
            }
        }
        return state
    }
}

