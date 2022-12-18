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
    private val billingRepository: BillingRepository):
    UseCase<CoroutineScope, Flow<SkuDetailsState>> {

    private val state = MutableStateFlow<SkuDetailsState>(SkuDetailsState.NotReady)

    override fun invoke(scope: CoroutineScope): Flow<SkuDetailsState> {
        scope.launch {
            billingRepository.observeBillingReadyStateFlow().collect { billingReadyState ->
                when(billingReadyState) {
                    is BillingReadyState.NoSkusExist -> state.emit(SkuDetailsState.NoSkusExist)
                    is BillingReadyState.NotReady -> state.emit(SkuDetailsState.NotReady)
                    is BillingReadyState.Ready -> {
                        processNewSkus()
                    }
                    is BillingReadyState.NewPurchasesAvailable -> {
                        processNewSkus()
                    }
                }
            }
        }
        return state
    }

    private suspend fun processNewSkus() {
        val skuList = billingRepository.getSkuDetails()
        processAnyBundlePurchases(skuList)
        state.emit(SkuDetailsState.Success(skuList))
    }

    private fun processAnyBundlePurchases(skuList: List<SkuDetailsDomain>) {
        val skuBundleBuyAll = skuList.find { it.isBundleBuyAll }
            skuBundleBuyAll?.let {
                if (it.isPurchased) {
                    // make everything else true.
                    skuList.forEach { it.isPurchased = true }
                } else {
                    // When all purchased skus are purchased, make the bundle purchased.
                    val nonBundleSkuList = skuList.filter { sku -> !sku.isBundleBuyAll }
                    val sizeOfNonPurchasedSkus = nonBundleSkuList.size
                    val sizeOfPurchasedSkus = nonBundleSkuList.filter { nonBundleSku -> nonBundleSku.isPurchased }.size
                    val hasAllNonBundleSkusBenPurchased = sizeOfNonPurchasedSkus == sizeOfPurchasedSkus
                    if (hasAllNonBundleSkusBenPurchased) {
                        skuBundleBuyAll.isPurchased = true
                    }
                }
        }
    }
}

