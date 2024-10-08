package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.owusu.cryptosignalalert.data.datasource.coingecko.paging.CoinsSource
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsThatHaveNotBeenHitUseCase
import com.owusu.cryptosignalalert.mappers.CoinDomainToUIMapper
import com.owusu.cryptosignalalert.models.*
import com.owusu.cryptosignalalert.util.PriceDisplayUtils
import com.owusu.cryptosignalalert.viewmodels.udf.UdfViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.coinslist.CoinsListUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.coinslist.CoinsListUdfEvent
import com.owusu.cryptosignalalert.viewmodels.udf.coinslist.CoinsListUiState
import com.owusu.cryptosignalalert.viewmodels.udf.coinslist.CoinsListUiStateMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val coinDomainToUIMapper: CoinDomainToUIMapper,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val getPriceTargetsThatHaveNotBeenHitUseCase: GetPriceTargetsThatHaveNotBeenHitUseCase,
    private val priceDisplayUtils: PriceDisplayUtils,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): UdfViewModel<CoinsListUdfEvent, CoinsListUiState, CoinsListUdfAction>(initialUiState = CoinsListUiState()) {

    private var currentPage = -1
    private val entireLoadedCoinMap = mutableListOf<CoinUI>()

    private val _isRefreshing = MutableStateFlow(false)

    val coinsListFlow = fetchCoinsList()

    init {
        listenForPriceTargetsUpdates()
    }

    private fun listenForPriceTargetsUpdates() {
        viewModelScope.launch {
            getPriceTargetsThatHaveNotBeenHitUseCase().collect { pt->
                var priceTargetsMap = mutableMapOf<String,PriceTargetDomain>()
                pt.associateByTo(priceTargetsMap) { it.id }
                entireLoadedCoinMap.forEach { coinUI ->
                    val priceTarget = priceTargetsMap[coinUI.id]
                    coinUI.apply {
                        // The list ui wont update on these two, since compose does not observe these.
                        // See CoinUI for more details
                        userPriceTarget = priceTarget?.userPriceTarget
                        userPriceTargetDisplay = priceDisplayUtils.convertPriceToString(priceTarget?.userPriceTarget)

                        // compose is observing this however.
                        hasPriceTarget.value = priceTarget != null // mutableState only update UI when changed.
                    }
                }
            }
        }
    }

    private fun fetchCoinsList(): Flow<PagingData<CoinUI>> {

        var coinsSource: CoinsSource? = null
        var priceTargetsMap = mutableMapOf<String,PriceTargetDomain>()

        return Pager(PagingConfig(pageSize = 1)) {
            coinsSource = CoinsSource(100, "usd", null, getCoinsListUseCase)
            coinsSource!!
        }.flow.map { pagingData ->
            pagingData.map { coinDomain ->
                if (coinDomain.id.equals("Rate_Limit_Reached")) {
                    setUiState {
                        val newUiState = uiState.value.copy(coinsListUiStateMessage = CoinsListUiStateMessage(
                            shouldShowMessage = true,
                            message = "Rate API limit reached. Try later.",
                            ctaText = "Dismiss"
                        )
                        )
                        newUiState
                    }
                }

                val pageNum = coinsSource!!.getCurrentPageNumber()

                if(currentPage != pageNum) {
                    currentPage = pageNum
                    // convert list to map more efficient if we search across a map
                    val priceTargetsList = getPriceTargetsThatHaveNotBeenHitUseCase().first()
                    priceTargetsList.associateByTo(priceTargetsMap) { it.id }
                }
                val coinUI = coinDomainToUIMapper.mapDomainToUI(coinDomain, priceTargetsMap)
                entireLoadedCoinMap.add(coinUI)
                coinUI
            }
        }.cachedIn(viewModelScope) // cache data even for config change screen rotation.
    }

    private fun hideSnackBar() {
        val newUiState = uiState.value.copy(coinsListUiStateMessage = CoinsListUiStateMessage(shouldShowMessage = false))
        setUiState { newUiState }
    }

    override fun handleEvent(event: CoinsListUdfEvent) {
        when(event) {
            CoinsListUdfEvent.HideSnackBar -> {
                hideSnackBar()
            }
        }
    }
}