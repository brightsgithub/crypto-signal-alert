package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.states.BillingReadyState
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.models.states.StartUpBillingState
import com.owusu.cryptosignalalert.domain.repository.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartupBillingUseCase(
    private val billingRepository: BillingRepository,
    private val refreshSkuDetailsUseCase: RefreshSkuDetailsUseCase):
    UseCase<CoroutineScope, Flow<StartUpBillingState>> {

    private val state = MutableStateFlow<StartUpBillingState>(StartUpBillingState.NotReady)

    override fun invoke(scope: CoroutineScope): Flow<StartUpBillingState> {
        scope.launch {
            billingRepository.observeBillingReadyStateFlow().collect { billingReadyState ->
                when(billingReadyState) {
                    is BillingReadyState.NoSkusExist -> state.emit(StartUpBillingState.Finished) // should be used only for startup billing usecase
                    is BillingReadyState.NotReady -> state.emit(StartUpBillingState.NotReady) // should be used only for startup billing usecase
                    is BillingReadyState.Ready -> {
                        processNewSkus()
                    }
                    is BillingReadyState.NewPurchasesAvailable -> {
                        state.emit(StartUpBillingState.Finished)
                    }
                }
            }
        }
        return state
    }

    private fun refresh(scope: CoroutineScope) {
        scope.launch {
            refreshSkuDetailsUseCase.invoke()
        }
    }

    private suspend fun processNewSkus() {
        val skuList = billingRepository.getSkuDetails()
        state.emit(StartUpBillingState.Finished)
    }
}