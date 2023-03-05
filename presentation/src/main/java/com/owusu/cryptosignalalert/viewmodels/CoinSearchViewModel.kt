package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.usecase.GetCoinDetailUseCase
import com.owusu.cryptosignalalert.domain.usecase.SearchCoinIdsUseCase
import com.owusu.cryptosignalalert.mappers.CoinDetailToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinIdToUIMapper
import com.owusu.cryptosignalalert.models.CoinIdUI
import com.owusu.cryptosignalalert.models.CoinSearchState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CoinSearchViewModel(private val searchCoinIdsUseCase: SearchCoinIdsUseCase,
                          private val coinIdToUIMapper: CoinIdToUIMapper,
                          private val dispatcherBackground: CoroutineDispatcher,
                          private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    //private var allUsers: ArrayList<CoinIdUI> = ArrayList<User>()
    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var matchedCoinIds: MutableStateFlow<List<CoinIdUI>> = MutableStateFlow(arrayListOf())
    private var searchJob: Job? = null

    val coinIdSearchModelState = combine(
        searchText,
        matchedCoinIds,
        showProgressBar
    ) { text, matchedIds, showProgress ->

        CoinSearchState(
            searchStr = text,
            coinIds = matchedIds,
            showProgress
        )
    }

    fun onSearchTextChanged(changedSearchText: String, scope: CoroutineScope = viewModelScope) {
        searchText.value = changedSearchText

        if (changedSearchText.isEmpty() || changedSearchText.length < 3) {
            // Don't execute the search if the query is less than 3 characters
            matchedCoinIds.value = emptyList()
            return
        }

        searchJob?.cancel() // Cancel the previous search job if it's still running
        searchJob = scope.launch {
            searchCoinIdsUseCase.invoke(SearchCoinIdsUseCase.Params(changedSearchText)).map { coinIdDomain ->
                matchedCoinIds.value = coinIdToUIMapper.map(coinIdDomain)
            }
        }
    }

    fun onClearClick() {
        searchText.value = ""
        matchedCoinIds.value = arrayListOf()
    }

}