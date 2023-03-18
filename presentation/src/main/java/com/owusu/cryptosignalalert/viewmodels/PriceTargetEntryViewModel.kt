package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.usecase.GetCoinDetailUseCase
import com.owusu.cryptosignalalert.domain.usecase.SaveNewPriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.SaveNewPriceTargetsWithLimitUseCase
import com.owusu.cryptosignalalert.mappers.CoinDetailToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinUIToPriceTargetDomainMapper
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.PriceEntryScreenEvents
import com.owusu.cryptosignalalert.models.PriceTargetEntryViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val _screenEvents= MutableSharedFlow<PriceEntryScreenEvents>() // for emitting
    val screenEvents: Flow<PriceEntryScreenEvents> = _screenEvents // for clients to listen to

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

    private suspend fun publishSaveStatus(wasPriceTargetSaved: Boolean) {
        if (wasPriceTargetSaved) {
            _screenEvents.emit(PriceEntryScreenEvents.SavePriceTargetSuccess)
        } else {
            _screenEvents.emit(PriceEntryScreenEvents.SavePriceTargetFailure)
        }
    }
}