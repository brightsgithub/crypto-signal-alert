package com.owusu.cryptosignalalert.viewmodels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.SkuDetailsState
import com.owusu.cryptosignalalert.domain.usecase.GetSkuDetailsUseCase
import com.owusu.cryptosignalalert.models.CoinUI
import kotlinx.coroutines.launch

class SharedViewModel(
    private val getSkuDetailsUseCase: GetSkuDetailsUseCase): ViewModel() {

    lateinit var selectedCoinUI: CoinUI

    fun getSkuDetails() {
        viewModelScope.launch {
            getSkuDetailsUseCase.invoke(this).collect {
                Log.v("SharedViewModel", "State is -> "+ it.toString())
                when(it) {
                    is SkuDetailsState.NotReady -> handleNotReadyYet()
                    is SkuDetailsState.Ready -> handleReady()
                    is SkuDetailsState.Success -> handleSkusList(it.skuList)
                }
            }
            Log.v("SharedViewModel", "AFTER")
        }
    }

    private fun handleReady() {
        // hide loading
    }

    private fun handleNotReadyYet() {
        // show loading
    }

    private fun handleSkusList(skuList: List<SkuDetailsDomain>) {
        skuList.forEach {
            Log.v("SharedViewModel", "SKUS FOUND = "+ it.toString())
        }
    }

}