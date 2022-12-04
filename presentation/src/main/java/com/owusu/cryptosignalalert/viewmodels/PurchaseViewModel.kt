package com.owusu.cryptosignalalert.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.usecase.BuySkyUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetSkuDetailsUseCase
import com.owusu.cryptosignalalert.mappers.SkuDetailsDomainToUIMapper
import com.owusu.cryptosignalalert.models.PurchaseViewState
import com.owusu.cryptosignalalert.models.SkuDetailsUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PurchaseViewModel(
    private val getSkuDetailsUseCase: GetSkuDetailsUseCase,
    private val buySkyUseCase: BuySkyUseCase,
    private val skuDetailsDomainToUIMapper: SkuDetailsDomainToUIMapper,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    init {
        Log.v("PurchaseViewModel", "PurchaseViewModel CREATED!")
    }

    private val _state = MutableStateFlow(PurchaseViewState()) // for emitting
    val viewState: Flow<PurchaseViewState> = _state // for clients to listen to

    private val _loadingState = MutableStateFlow(true) // for emitting
    val loadingState: Flow<Boolean> = _loadingState // for clients to listen to

    fun loadSkuDetails() {
        viewModelScope.launch(dispatcherBackground) {
            getSkuDetailsUseCase.invoke(this).collect {
                when(it) {
                    is SkuDetailsState.NotReady -> handleNotReadyYet()
                    is SkuDetailsState.NoSkusExist -> noSkusExist()
                    is SkuDetailsState.Success -> handleSkusList(it.skuList)
                }
            }
        }
    }

    private suspend fun noSkusExist() {
        withContext(dispatcherMain) {
            Log.v("PurchaseViewModel", "handleReady()")
            hideLoading()
        }
    }

    private suspend fun handleNotReadyYet() {
        withContext(dispatcherMain) {
            Log.v("PurchaseViewModel", "handleNotReadyYet()")
            showLoading()
        }
    }

    private suspend fun handleSkusList(skuList: List<SkuDetailsDomain>) {
        withContext(dispatcherMain) {
            hideLoading()
            val skuUIList = skuDetailsDomainToUIMapper.mapToUI(skuList)
            _state.value = _state.value.copy(skuDetailsList = skuUIList)
        }
    }

    fun buyProduct(screenProxy: ScreenProxy, sku: String) {
        viewModelScope.launch {
            buySkyUseCase.invoke(BuySkyUseCase.Params(screenProxy, sku))
        }
    }

    private fun showLoading() {
        _loadingState.value = true
    }

    private fun hideLoading() {
        _loadingState.value = false
    }
}