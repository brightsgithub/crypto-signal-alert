package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.PurchaseConstants.MIN_PRICE_TARGET_RECORDS_ALLOWED
import com.owusu.cryptosignalalert.domain.usecase.GetCoinDetailUseCase
import com.owusu.cryptosignalalert.domain.usecase.SaveNewPriceTargetsWithLimitUseCase
import com.owusu.cryptosignalalert.mappers.CoinDetailToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinUIToPriceTargetDomainMapper
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.PriceTargetEntryViewState
import com.owusu.cryptosignalalert.models.PriceTargetsMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PriceTargetEntryViewModel(
    private val coinUIToPriceTargetDomainMapper: CoinUIToPriceTargetDomainMapper,
    private val saveNewPriceTargetsWithLimitUseCase: SaveNewPriceTargetsWithLimitUseCase,
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val coinDetailToUIMapper: CoinDetailToUIMapper,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(PriceTargetEntryViewState()) // for emitting
    val viewState: Flow<PriceTargetEntryViewState> = _state // for clients to listen to
    fun getCoinDetails(scope: CoroutineScope = viewModelScope, coinUI: CoinUI) {
        scope.launch(dispatcherBackground) {
            _state.value = _state.value.copy(isLoading = true)
            val coinDetailDomain = getCoinDetailUseCase.invoke(GetCoinDetailUseCase.Params(coinUI.id))
            val coinDetailUI = coinDetailToUIMapper.map(coinDetailDomain)
            _state.value = PriceTargetEntryViewState(coinDetailUI = coinDetailUI, isLoading = false)
        }
    }

    fun saveNewPriceTarget(coinUI: CoinUI, userPriceTarget: String) {

        if (userPriceTarget.isBlank()) return

        viewModelScope.launch {
            val priceTargetDomain = coinUIToPriceTargetDomainMapper.mapUIToDomain(coinUI, userPriceTarget)
            val priceTargetDomainList = listOf(priceTargetDomain)
            val params = SaveNewPriceTargetsWithLimitUseCase.Params(priceTargetDomainList)
            val wasPriceTargetSaved = saveNewPriceTargetsWithLimitUseCase.invoke(params = params)
            publishSaveStatus(wasPriceTargetSaved)
        }
    }

    private fun publishSaveStatus(wasPriceTargetSaved: Boolean) {
        if (wasPriceTargetSaved) {
            _state.value = _state.value.copy(
                priceTargetsMessage = PriceTargetsMessage (
                    shouldShowMessage = true,
                    isError = false,
                    message = "Price target added",
                    ctaText = "Dismiss"
                )
            )
        } else {
            _state.value = _state.value.copy(
                priceTargetsMessage = PriceTargetsMessage (
                    shouldShowMessage = true,
                    isError = true,
                    message = "Max limit of "+MIN_PRICE_TARGET_RECORDS_ALLOWED+" targets",
                    ctaText = "Unlock?"
                )
            )
        }
    }
}