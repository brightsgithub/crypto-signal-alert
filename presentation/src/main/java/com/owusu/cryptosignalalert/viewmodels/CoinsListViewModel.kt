package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.mappers.UIListMapper
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.CoinsListUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val coinDomainToUIMapper: UIListMapper<CoinDomain, CoinUI>,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow<CoinsListUiState>(CoinsListUiState()) // for emitting
    val viewState: Flow<CoinsListUiState> = _state // for clients to listen to

    fun loadCoinsList(
        page: Int,
        recordsPerPage: Int
    ) {
        viewModelScope.launch(dispatcherBackground) {
            val params = GetCoinsListUseCase.Params(
                page = page,
                recordsPerPage = recordsPerPage
            )
            val coinsListDomain = getCoinsListUseCase.invoke(params)
            val coinsListUI = coinDomainToUIMapper.mapDomainListToUIList(coinsListDomain)
            _state.value = _state.value.copy(coins = coinsListUI)
        }
    }
}