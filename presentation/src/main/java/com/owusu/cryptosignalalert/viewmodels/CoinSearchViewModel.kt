package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.CoinsListResult
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.domain.usecase.SearchCoinIdsUseCase
import com.owusu.cryptosignalalert.mappers.CoinDomainToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinIdToUIMapper
import com.owusu.cryptosignalalert.models.*
import com.owusu.cryptosignalalert.viewmodels.udf.UdfViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.coinserach.CoinSearchUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.coinserach.CoinSearchUdfEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoinSearchViewModel(
    private val searchCoinIdsUseCase: SearchCoinIdsUseCase,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val coinDomainToUIMapper: CoinDomainToUIMapper,
    private val coinIdToUIMapper: CoinIdToUIMapper,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
) : UdfViewModel<CoinSearchUdfEvent, CoinSearchState, CoinSearchUdfAction>(initialUiState = CoinSearchState()) {

    private var searchJob: Job? = null

    private fun onSearchTextChanged(changedSearchText: String, scope: CoroutineScope = viewModelScope) {

        var newUiState: CoinSearchState
        newUiState = uiState.value.copy(searchStr = changedSearchText)
        setUiState { newUiState }

        if (changedSearchText.isEmpty() || changedSearchText.length < 3) {
            // Don't execute the search if the query is less than 3 characters
            newUiState = newUiState.copy(coinIds = emptyList())
            setUiState { newUiState }
            return
        }

        newUiState = newUiState.copy(showProgressBar = true)
        setUiState { newUiState }
        searchJob?.cancel() // Cancel the previous search job if it's still running
        searchJob = scope.launch {
            searchCoinIdsUseCase.invoke(SearchCoinIdsUseCase.Params(changedSearchText))
                .collect { coinIdList ->
                    newUiState = newUiState.copy(resultSize = coinIdList.size, coinIds = coinIdToUIMapper.map(coinIdList))
                    setUiState { newUiState }
                }
            newUiState = newUiState.copy(showProgressBar = false)
            setUiState { newUiState }
        }
    }

    private fun onSearchItemSelected(
        coinIdUI: CoinIdUI,
        scope: CoroutineScope = viewModelScope,
    ) {
        scope.launch {
            var newUiState = uiState.value.copy(showProgressBar = true)
            setUiState { newUiState }

            val params = GetCoinsListUseCase.Params(ids = coinIdUI.id)

            when (val result = getCoinsListUseCase.invoke(params)) {
                is CoinsListResult.Error -> {
                    newUiState = newUiState.copy(
                        coinSearchStateMessage = CoinSearchStateMessage(
                            shouldShowMessage = true,
                            message = "Rate limit reached. Try later",
                            ctaText = "Dismiss"
                        )
                    )
                    setUiState { newUiState }
                }

                is CoinsListResult.Success -> {
                    result.coinDomainList.firstOrNull()?.let {
                        val coinDomainUI = coinDomainToUIMapper.mapDomainToUI(it)
                        sendAction {
                            CoinSearchUdfAction.NavigateToPriceTargetEntryScreen(
                                coinDomainUI
                            )
                        }

                        hideSnackBar()
                        newUiState = newUiState.copy(showProgressBar = false)
                        setUiState { newUiState }
                    }
                }
            }
        }
    }

    private fun onClearClick() {
        var newUiState = uiState.value.copy(searchStr = "")
        newUiState = newUiState.copy(coinIds = emptyList())
        setUiState { newUiState }
    }

    private fun hideSnackBar() {
         val newUiState = uiState.value.copy(
             coinSearchStateMessage = CoinSearchStateMessage(shouldShowMessage = false))
        setUiState { newUiState }
    }

    override fun handleEvent(event: CoinSearchUdfEvent) {
        when (event) {
            is CoinSearchUdfEvent.OnSearchTextChanged -> onSearchTextChanged(event.changedSearchText)
            is CoinSearchUdfEvent.OnSearchItemSelected -> onSearchItemSelected(event.coinIdUI)
            is CoinSearchUdfEvent.OnClearClicked -> onClearClick()
            is CoinSearchUdfEvent.HideSnackBar -> hideSnackBar()
        }
    }
}