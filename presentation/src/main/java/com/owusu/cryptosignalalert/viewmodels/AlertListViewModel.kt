package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetsWrapper
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsUseCase
import com.owusu.cryptosignalalert.mappers.UIListMapper
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.AlertListUIWrapper
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.models.PriceTargetWrapperUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertListViewModel(
    private val priceTargetsMapper: UIListMapper<PriceTargetDomain, PriceTargetUI>,
    private val getPriceTargetsUseCase: GetPriceTargetsUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
    ): ViewModel() {

    private val _state = MutableSharedFlow<AlertListViewState>() // for emitting
    val viewState: Flow<AlertListViewState> = _state // for clients to listen to

    fun loadAlertList(scope: CoroutineScope = viewModelScope) {
        scope.launch(dispatcherBackground) {
            showLoadingState()
            val alertList = getPriceTargetsUseCase.invoke()
            handleAlertList(alertList)
        }
    }

    private suspend fun showLoadingState() {
        withContext(dispatcherMain) {
            _state.emit(AlertListViewState.ShowLoadingState)
        }
    }

    private suspend fun hideLoadingState() {
        withContext(dispatcherMain) {
            _state.emit(AlertListViewState.HideLoadingState)
        }
    }

    private suspend fun handleAlertList(priceTargetsDomainList: List<PriceTargetDomain>) {
        withContext(dispatcherMain) {
            val alertListUIWrapper = priceTargetsMapper.mapDomainListToUIList(priceTargetsDomainList)
            _state.emit(AlertListViewState.AlertListDataSuccess(alertListUIWrapper))
        }
    }
}