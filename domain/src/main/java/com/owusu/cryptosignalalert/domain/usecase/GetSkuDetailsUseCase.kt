package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.BillingReadyState
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.repository.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetSkuDetailsUseCase(
    private val billingRepository: BillingRepository,
    private val refreshSkuDetailsUseCase: RefreshSkuDetailsUseCase):
    UseCase<CoroutineScope, Flow<SkuDetailsState>> {

    private val state = MutableStateFlow<SkuDetailsState>(SkuDetailsState.NotReady)

    override fun invoke(scope: CoroutineScope): Flow<SkuDetailsState> {
        scope.launch {
            billingRepository.observeBillingReadyStateFlow().collect { billingReadyState ->
                when(billingReadyState) {
                    is BillingReadyState.NoSkusExist -> state.emit(SkuDetailsState.NoSkusExist) // should be used only for startup billing usecase
                    is BillingReadyState.NotReady -> state.emit(SkuDetailsState.NotReady) // should be used only for startup billing usecase
                    is BillingReadyState.Ready -> {
                        processNewSkus()
                    }
                    is BillingReadyState.NewPurchasesAvailable -> {
                        refresh(this)
                    }
                }
            }
        }

        // Billing should be called on startUp so this will no longer be a double load, since
        // when billing is called on start up, it will already have initialised. Calling the above
        // GetSkuDetailsUseCase will simply observe first and then refresh which triggers a query to load
        refresh(scope)

        return state
    }

    private fun refresh(scope: CoroutineScope) {
        scope.launch {
            refreshSkuDetailsUseCase.invoke()
        }
    }

    private suspend fun processNewSkus() {
        val skuList = billingRepository.getSkuDetails()
        processAnyBundlePurchases(skuList)
        state.emit(SkuDetailsState.Success(skuList))
    }

    private fun processAnyBundlePurchases(skuList: List<SkuDetailsDomain>) {
        val skuBundleBuyAll = skuList.find { it.isBundleBuyAll }
            skuBundleBuyAll?.let { it ->
                if (it.isPurchased) {
                    // make everything else true.
                    skuList.forEach { sku -> sku.isPurchased = true }
                } else {
                    // When all purchased skus are purchased, make the bundle purchased.
                    val nonBundleSkuList = skuList.filter { sku -> !sku.isBundleBuyAll }
                    val sizeOfNonPurchasedSkus = nonBundleSkuList.size
                    val sizeOfPurchasedSkus = nonBundleSkuList.filter { nonBundleSku -> nonBundleSku.isPurchased }.size //
                    val hasAllNonBundleSkusBenPurchased = sizeOfNonPurchasedSkus == sizeOfPurchasedSkus
                    if (hasAllNonBundleSkusBenPurchased) {
                        skuBundleBuyAll.isPurchased = true
                    }
                }
        }
    }
}

