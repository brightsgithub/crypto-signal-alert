package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.CoinsListResult
import com.owusu.cryptosignalalert.domain.usecase.GetCoinDetailUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.domain.usecase.SearchCoinIdsUseCase
import com.owusu.cryptosignalalert.mappers.CoinDetailToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinDomainToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinIdToUIMapper
import com.owusu.cryptosignalalert.models.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoinSearchViewModel(private val searchCoinIdsUseCase: SearchCoinIdsUseCase,
                          private val getCoinsListUseCase: GetCoinsListUseCase,
                          private val coinDomainToUIMapper: CoinDomainToUIMapper,
                          private val coinIdToUIMapper: CoinIdToUIMapper,
                          private val dispatcherBackground: CoroutineDispatcher,
                          private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    //private var allUsers: ArrayList<CoinIdUI> = ArrayList<User>()
    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var matchedCoinIds: MutableStateFlow<List<CoinIdUI>> = MutableStateFlow(arrayListOf())
    private var resultSize: MutableStateFlow<Int> = MutableStateFlow(0)
    private var coinSearchStateMessage: MutableStateFlow<CoinSearchStateMessage> = MutableStateFlow(CoinSearchStateMessage())

    private val _coinSearchStateEvents: MutableStateFlow<CoinSearchStateEvents> = MutableStateFlow(CoinSearchStateEvents.NOTHING)
    val coinSearchStateEvents: Flow<CoinSearchStateEvents> = _coinSearchStateEvents

    private var searchJob: Job? = null

    val coinIdSearchModelState = combine(
        searchText,
        matchedCoinIds,
        showProgressBar,
        resultSize,
        coinSearchStateMessage
    ) { text, matchedIds, showProgress, resultSize, coinSearchStateMessage ->

        // refactor this state approach
        CoinSearchState(
            searchStr = text,
            coinIds = matchedIds,
            showProgressBar = showProgress,
            resultSize = resultSize,
            coinSearchStateMessage = coinSearchStateMessage
        )
    }

    fun onSearchTextChanged(changedSearchText: String, scope: CoroutineScope = viewModelScope) {
        searchText.value = changedSearchText

        if (changedSearchText.isEmpty() || changedSearchText.length < 3) {
            // Don't execute the search if the query is less than 3 characters
            matchedCoinIds.value = emptyList()
            return
        }
        showProgressBar.value = true
        searchJob?.cancel() // Cancel the previous search job if it's still running
        searchJob = scope.launch {
            searchCoinIdsUseCase.invoke(SearchCoinIdsUseCase.Params(changedSearchText)).collect { coinIdList ->
                resultSize.value = coinIdList.size
                matchedCoinIds.value = coinIdToUIMapper.map(coinIdList)
            }
            showProgressBar.value = false
        }
    }

    fun onSearchItemSelected(
        coinIdUI: CoinIdUI,
        scope: CoroutineScope = viewModelScope,
    ) {
        scope.launch {
            showProgressBar.value = true
            val params = GetCoinsListUseCase.Params(ids = coinIdUI.id)

            when (val result = getCoinsListUseCase.invoke(params)) {
                is CoinsListResult.Error -> {

                    coinSearchStateMessage.value = CoinSearchStateMessage(
                        shouldShowMessage = true,
                        message = "Rate limit reached. Try later",
                        ctaText = "Dismiss"
                    )
                }
                is CoinsListResult.Success -> {
                        result.coinDomainList.firstOrNull()?.let {

                            val coinDomainUI = coinDomainToUIMapper.mapDomainToUI(it)
                            _coinSearchStateEvents.value = CoinSearchStateEvents.NavigateToPriceTargetEntryScreen(coinDomainUI)
                            hideSnackBar()
                    }
                }
            }

            showProgressBar.value = false
        }
    }

    fun onClearClick() {
        searchText.value = ""
        matchedCoinIds.value = arrayListOf()
    }

    fun hideSnackBar() {
        coinSearchStateMessage.value = CoinSearchStateMessage(shouldShowMessage = false)
    }
}