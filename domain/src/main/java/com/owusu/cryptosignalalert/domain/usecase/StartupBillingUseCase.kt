package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.ProductKeys
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.BillingReadyState
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.models.states.StartUpBillingState
import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository
import com.owusu.cryptosignalalert.domain.repository.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartupBillingUseCase(
    private val billingRepository: BillingRepository,
    private val refreshSkuDetailsUseCase: RefreshSkuDetailsUseCase,
    private val appPreferences: AppPreferencesRepository):
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
        savePurchasedPreferences()
        state.emit(StartUpBillingState.Finished)
    }

    private suspend fun savePurchasedPreferences() {

        val skuList = billingRepository.getSkuDetails()
        val skuIdsMap = billingRepository.getSkuIdsMap()

        System.out.println("StartupBillingUseCase_1 "+ appPreferences.isAppFree())
        System.out.println("StartupBillingUseCase_1 "+ appPreferences.isPriceTargetLimitPurchased())
        System.out.println("StartupBillingUseCase_1 "+ appPreferences.isAdsPurchased())

        for(skuDetail: SkuDetailsDomain in skuList) {
            when(skuDetail.sku) {
                skuIdsMap[ProductKeys.UNLOCK_ALL] -> {
                    if (skuDetail.isPurchased) {
                        makeEverythingFree()
                        break
                    }
                    else {
                        appPreferences.makeAppPurchasable()
                    }
                }
                skuIdsMap[ProductKeys.UNLIMITED_ALERTS] -> {
                    if (skuDetail.isPurchased) {
                        appPreferences.makePriceTargetLimitFree()
                    }
                    else {
                        appPreferences.makePriceTargetLimitPurchasable()
                    }
                }
                skuIdsMap[ProductKeys.REMOVE_ADS] -> {
                    if (skuDetail.isPurchased) {
                        appPreferences.makeAdsFree()
                    }
                    else {
                        appPreferences.makeAdsPurchasable()
                    }
                }
            }
        }

        // TODO
        // ############################## REMOVE!!!!!!
        //makeEverythingFree()

        System.out.println("StartupBillingUseCase_2 "+ appPreferences.isAppFree())
        System.out.println("StartupBillingUseCase_2 "+ appPreferences.isPriceTargetLimitPurchased())
        System.out.println("StartupBillingUseCase_2 "+ appPreferences.isAdsPurchased())
    }

    private fun makeEverythingFree() {
        appPreferences.makeAppFree()
        appPreferences.makePriceTargetLimitFree()
        appPreferences.makeAdsFree()
    }
}