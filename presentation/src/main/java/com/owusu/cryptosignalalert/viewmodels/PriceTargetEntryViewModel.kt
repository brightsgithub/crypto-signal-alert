package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.PurchaseConstants.MIN_PRICE_TARGET_RECORDS_ALLOWED
import com.owusu.cryptosignalalert.domain.usecase.GetCoinDetailUseCase
import com.owusu.cryptosignalalert.domain.usecase.SaveNewPriceTargetsWithLimitUseCase
import com.owusu.cryptosignalalert.mappers.CoinDetailToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinUIToPriceTargetDomainMapper
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.udf.pricetargetentry.PriceTargetEntryViewState
import com.owusu.cryptosignalalert.viewmodels.udf.pricetargetentry.PriceTargetsMessage
import com.owusu.cryptosignalalert.viewmodels.udf.UdfViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.pricetargetentry.PriceTargetEntryUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.pricetargetentry.PriceTargetEntryUdfEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PriceTargetEntryViewModel(
    private val coinUIToPriceTargetDomainMapper: CoinUIToPriceTargetDomainMapper,
    private val saveNewPriceTargetsWithLimitUseCase: SaveNewPriceTargetsWithLimitUseCase,
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val coinDetailToUIMapper: CoinDetailToUIMapper,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
) : UdfViewModel<PriceTargetEntryUdfEvent, PriceTargetEntryViewState, PriceTargetEntryUdfAction>(
    initialUiState = PriceTargetEntryViewState()
) {

    private fun getCoinDetails(scope: CoroutineScope = viewModelScope, coinUI: CoinUI) {
        scope.launch(dispatcherBackground) {
            setUiState {
                val newUIState = uiState.value.copy(isLoading = true)
                newUIState
            }
            val coinDetailDomain =
                getCoinDetailUseCase.invoke(GetCoinDetailUseCase.Params(coinUI.id))
            val coinDetailUI = coinDetailToUIMapper.map(coinDetailDomain)

            setUiState {
                val newUIState =
                    PriceTargetEntryViewState(coinDetailUI = coinDetailUI, isLoading = false)
                newUIState
            }
        }
    }

    private fun saveNewPriceTarget(coinUI: CoinUI, userPriceTarget: String) {

        if (userPriceTarget.isBlank()) return

        viewModelScope.launch {
            val priceTargetDomain =
                coinUIToPriceTargetDomainMapper.mapUIToDomain(coinUI, userPriceTarget)
            val priceTargetDomainList = listOf(priceTargetDomain)
            val params = SaveNewPriceTargetsWithLimitUseCase.Params(priceTargetDomainList)
            val wasPriceTargetSaved = saveNewPriceTargetsWithLimitUseCase.invoke(params = params)
            publishSaveStatus(wasPriceTargetSaved)
        }
    }

    private fun publishSaveStatus(wasPriceTargetSaved: Boolean) {
        if (wasPriceTargetSaved) {
            val newUIState = uiState.value.copy(
                priceTargetsMessage = PriceTargetsMessage(
                    shouldShowMessage = true,
                    isError = false,
                    message = "Price target added",
                    ctaText = "Dismiss"
                )
            )
            setUiState { newUIState }
        } else {
            val newUIState = uiState.value.copy(
                priceTargetsMessage = PriceTargetsMessage(
                    shouldShowMessage = true,
                    isError = true,
                    message = "Max limit of " + MIN_PRICE_TARGET_RECORDS_ALLOWED + " targets",
                    ctaText = "Unlock?"
                )
            )
            setUiState { newUIState }
        }
    }

    override fun handleEvent(event: PriceTargetEntryUdfEvent) {
        when(event) {
            is PriceTargetEntryUdfEvent.GetCoinDetails -> {
                getCoinDetails(coinUI = event.coinUI)
            }
            is PriceTargetEntryUdfEvent.SaveNewPriceTarget -> {
                saveNewPriceTarget(coinUI = event.coinUI, userPriceTarget = event.userPriceTarget)
                sendAction { PriceTargetEntryUdfAction.NavigateToPriceTargetsAppEntry }
            }
        }
    }
}