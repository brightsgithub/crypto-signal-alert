package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.usecase.SaveNewPriceTargetsUseCase
import com.owusu.cryptosignalalert.mappers.CoinUIToPriceTargetDomainMapper
import com.owusu.cryptosignalalert.models.CoinUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class PriceTargetEntryViewModel(
    private val coinUIToPriceTargetDomainMapper: CoinUIToPriceTargetDomainMapper,
    private val saveNewPriceTargetsUseCase: SaveNewPriceTargetsUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    fun saveNewPriceTarget(coinUI: CoinUI, userPriceTarget: String) {

        if (userPriceTarget.isBlank()) return

        viewModelScope.launch {
            val priceTargetDomain = coinUIToPriceTargetDomainMapper.mapUIToDomain(coinUI, userPriceTarget)
            val priceTargetDomainList = listOf(priceTargetDomain)
            val params = SaveNewPriceTargetsUseCase.Params(priceTargetDomainList)
            saveNewPriceTargetsUseCase.invoke(params = params)
        }
    }
}