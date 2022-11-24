package com.owusu.cryptosignalalert.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.usecase.GetSkuDetailsUseCase
import com.owusu.cryptosignalalert.mappers.SkuDetailsDomainToUIMapper
import com.owusu.cryptosignalalert.models.PurchaseViewState
import com.owusu.cryptosignalalert.models.SkuDetailsUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PurchaseViewModel(
    private val getSkuDetailsUseCase: GetSkuDetailsUseCase,
    private val skuDetailsDomainToUIMapper: SkuDetailsDomainToUIMapper
): ViewModel() {
    private val _state = MutableStateFlow(PurchaseViewState()) // for emitting
    val viewState: Flow<PurchaseViewState> = _state // for clients to listen to

    fun loadSkuDetails() {
        viewModelScope.launch {
            getSkuDetailsUseCase.invoke(this).collect {
                when(it) {
                    is SkuDetailsState.NotReady -> handleNotReadyYet()
                    is SkuDetailsState.Ready -> handleReady()
                    is SkuDetailsState.Success -> handleSkusList(it.skuList)
                }
            }
        }
    }

    private fun handleReady() {
        // hide loading
        Log.v("PurchaseViewModel", "handleReady()")
    }

    private fun handleNotReadyYet() {
        // show loading
        Log.v("PurchaseViewModel", "handleNotReadyYet()")
    }

    private fun handleSkusList(skuList: List<SkuDetailsDomain>) {
        val skuUIList = skuDetailsDomainToUIMapper.mapToUI(skuList)
        _state.value = _state.value.copy(skuDetailsList = skuUIList)
    }

    fun buyProduct(sku: SkuDetailsUI) {
        Log.v("PurchaseViewModel", "buyProduct clicked -> " + sku.toString())
    }
}