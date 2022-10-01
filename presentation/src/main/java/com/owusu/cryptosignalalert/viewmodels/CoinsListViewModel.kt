package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.owusu.cryptosignalalert.data.datasource.coingecko.paging.CoinsSource
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.CoinsListUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val coinDomainToUIMapper: UIMapper<CoinDomain, CoinUI>,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow<CoinsListUiState>(CoinsListUiState()) // for emitting
    val viewState: Flow<CoinsListUiState> = _state // for clients to listen to
    val coinsListFlow = fetchCoinsList()

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

    private fun fetchCoinsList(): Flow<PagingData<CoinUI>> {
        return Pager(PagingConfig(pageSize = 1)) {
            CoinsSource(100, "usd", null, getCoinsListUseCase)
        }.flow.map {
            it.map { coinDomain -> coinDomainToUIMapper.mapDomainToUI(coinDomain) }
        }.cachedIn(viewModelScope) // cache data even for config change screen rotation.
    }
}