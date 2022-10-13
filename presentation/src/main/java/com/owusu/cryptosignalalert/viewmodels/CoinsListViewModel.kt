package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.owusu.cryptosignalalert.data.datasource.coingecko.paging.CoinsSource
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsThatHaveNotBeenHitUseCase
import com.owusu.cryptosignalalert.mappers.CoinDomainToUIMapper
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.CoinsListUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class CoinsListViewModel(
    private val coinDomainToUIMapper: CoinDomainToUIMapper,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val getPriceTargetsThatHaveNotBeenHitUseCase: GetPriceTargetsThatHaveNotBeenHitUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow<CoinsListUiState>(CoinsListUiState()) // for emitting
    val viewState: Flow<CoinsListUiState> = _state // for clients to listen to
    val coinsListFlow = fetchCoinsList()
    private var currentPage = -1


    private fun fetchCoinsList(): Flow<PagingData<CoinUI>> {

        var coinsSource: CoinsSource? = null
        var priceTargetsMap = mutableMapOf<String,PriceTargetDomain>()

        return Pager(PagingConfig(pageSize = 1)) {
            coinsSource = CoinsSource(100, "usd", null, getCoinsListUseCase)
            coinsSource!!
        }.flow.map { pagingData ->
            pagingData.map { coinDomain ->
                val pageNum = coinsSource!!.getCurrentPageNumber()
                if(currentPage != pageNum) {
                    currentPage = pageNum
                    // convert list to map more efficient if we search across a map
                    val priceTargets = getPriceTargetsThatHaveNotBeenHitUseCase()
                    priceTargets.associateByTo(priceTargetsMap) {it.id}
                }
                coinDomainToUIMapper.mapDomainToUI(coinDomain, priceTargetsMap)
            }
        }.cachedIn(viewModelScope) // cache data even for config change screen rotation.
    }
}