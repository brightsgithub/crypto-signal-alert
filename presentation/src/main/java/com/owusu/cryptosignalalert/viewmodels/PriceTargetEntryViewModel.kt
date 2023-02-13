package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.usecase.GetCoinDetailUseCase
import com.owusu.cryptosignalalert.domain.usecase.SaveNewPriceTargetsUseCase
import com.owusu.cryptosignalalert.mappers.CoinUIToPriceTargetDomainMapper
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.PriceEntryScreenEvents
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class PriceTargetEntryViewModel(
    private val coinUIToPriceTargetDomainMapper: CoinUIToPriceTargetDomainMapper,
    private val saveNewPriceTargetsUseCase: SaveNewPriceTargetsUseCase,
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    private val _screenEvents= MutableSharedFlow<PriceEntryScreenEvents>() // for emitting
    val screenEvents: Flow<PriceEntryScreenEvents> = _screenEvents // for clients to listen to

    fun saveNewPriceTarget(coinUI: CoinUI, userPriceTarget: String) {

        if (userPriceTarget.isBlank()) return

        viewModelScope.launch {
            val priceTargetDomain = coinUIToPriceTargetDomainMapper.mapUIToDomain(coinUI, userPriceTarget)
            val priceTargetDomainList = listOf(priceTargetDomain)
            val params = SaveNewPriceTargetsUseCase.Params(priceTargetDomainList)
            val wasPriceTargetSaved = saveNewPriceTargetsUseCase.invoke(params = params)
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