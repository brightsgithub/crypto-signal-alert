package com.owusu.cryptosignalalert.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.usecase.BuySkyUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetSkuDetailsUseCase
import com.owusu.cryptosignalalert.domain.usecase.RefreshSkuDetailsUseCase
import com.owusu.cryptosignalalert.mappers.SkuDetailsDomainToUIMapper
import com.owusu.cryptosignalalert.viewmodels.udf.purchase.PurchaseViewState
import com.owusu.cryptosignalalert.viewmodels.udf.UdfViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.purchase.PurchaseUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.purchase.PurchaseUdfEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PurchaseViewModel(
    private val getSkuDetailsUseCase: GetSkuDetailsUseCase,
    private val refreshSkuDetailsUseCase: RefreshSkuDetailsUseCase,
    private val buySkyUseCase: BuySkyUseCase,
    private val skuDetailsDomainToUIMapper: SkuDetailsDomainToUIMapper,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): UdfViewModel<PurchaseUdfEvent, PurchaseViewState, PurchaseUdfAction>(initialUiState = PurchaseViewState()) {

    init {
        Log.v("PurchaseViewModel", "PurchaseViewModel CREATED!")
    }

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
            val newUiState = uiState.value.copy(skuDetailsList = skuUIList)
            setUiState { newUiState }
        }
    }

    private fun buyProduct(screenProxy: ScreenProxy, sku: String) {
        viewModelScope.launch {
            buySkyUseCase.invoke(BuySkyUseCase.Params(screenProxy, sku))
        }
    }

    private fun showLoading() {
        val newUiState = uiState.value.copy(isLoading = true)
        setUiState { newUiState }
    }

    private fun hideLoading() {
        val newUiState = uiState.value.copy(isLoading = false)
        setUiState { newUiState }
    }

    override fun handleEvent(event: PurchaseUdfEvent) {
        when(event) {
            is PurchaseUdfEvent.OnPurchaseClicked -> {
                buyProduct(event.screenProxy, event.sku)
            }
            is PurchaseUdfEvent.LoadSkuDetails -> {
                loadSkuDetails()
            }
        }
    }
}