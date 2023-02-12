package com.owusu.cryptosignalalert.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.states.BillingReadyState
import com.owusu.cryptosignalalert.domain.models.states.StartUpBillingState
import com.owusu.cryptosignalalert.domain.usecase.PopulateCoinIdsUseCase
import com.owusu.cryptosignalalert.domain.usecase.StartupBillingUseCase
import com.owusu.cryptosignalalert.models.CoinUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SharedViewModel(
    private val startupBillingUseCase: StartupBillingUseCase,
    private val populateCoinIdsUseCase: PopulateCoinIdsUseCase
    ): ViewModel() {

    fun initApp() {
        viewModelScope.launch {
            populateCoinIds()
            initBillingOnStartUp(this)
        }
    }

    private suspend fun populateCoinIds() {
        populateCoinIdsUseCase.invoke()
    }

    /**
     * Do what we need to do in terms of show hide based on the state of iap.
     */
    private suspend fun initBillingOnStartUp(scope: CoroutineScope) {
        startupBillingUseCase.invoke(scope).collect { startUpBillingState ->
            when (startUpBillingState) {
                StartUpBillingState.NotReady -> {
                    Log.d("SharedViewModel", "BillingReadyState.NotReady")
                }
                StartUpBillingState.Finished -> {
                    Log.d("SharedViewModel", "BillingReadyState.Ready")
                }
            }
        }
    }

    lateinit var selectedCoinUI: CoinUI

}