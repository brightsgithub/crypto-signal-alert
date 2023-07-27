package com.owusu.cryptosignalalert.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.states.PurchasedStateDomain
import com.owusu.cryptosignalalert.domain.models.states.StartUpBillingState
import com.owusu.cryptosignalalert.domain.usecase.PopulateCoinIdsUseCase
import com.owusu.cryptosignalalert.domain.usecase.SavedPurchasedStateChangesUseCase
import com.owusu.cryptosignalalert.domain.usecase.StartupBillingUseCase
import com.owusu.cryptosignalalert.models.AppSnackBar
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.SharedViewState
import com.owusu.cryptosignalalert.resource.AppStringProvider
import com.owusu.cryptosignalalert.viewmodels.helpers.ToolBarHelper
import com.owusu.cryptosignalalert.views.screens.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class SharedViewModel(
    private val startupBillingUseCase: StartupBillingUseCase,
    private val populateCoinIdsUseCase: PopulateCoinIdsUseCase,
    private val savedPurchasedStateChangesUseCase: SavedPurchasedStateChangesUseCase,
    private val appStringProvider: AppStringProvider,
    private val toolBarHelper: ToolBarHelper
    ): ViewModel() {

    private val _sharedViewState = MutableStateFlow(SharedViewState()) // for emitting
    val sharedViewState: Flow<SharedViewState> = _sharedViewState // for clients to listen to

    init {
        toolBarHelper.initToolBarHelper(state = _sharedViewState)
    }

    fun initApp() {
        viewModelScope.launch {
            populateCoinIds()

            launch {
                savedPurchasedStateChangesUseCase.invoke().collect { purchasedStateDomain ->
                    updatePurchasedState(purchasedStateDomain)
                    printCurrentState()
                }
            }

            initBillingOnStartUp(this)
        }
    }

    private fun updatePurchasedState(purchasedStateDomain: PurchasedStateDomain) {
        Log.d("SharedViewModel", "updatePurchasedState1 = "+ purchasedStateDomain)
        _sharedViewState.value = _sharedViewState.value.copy(
            purchasedState = _sharedViewState.value.purchasedState.copy(
                isAppFree = purchasedStateDomain.isAppFree,
                isAdsPurchased = purchasedStateDomain.isAdsPurchased,
                isPriceTargetLimitPurchased = purchasedStateDomain.isPriceTargetLimitPurchased
            )
        )

        Log.d("SharedViewModel", "updatePurchasedState2 = "+ _sharedViewState.value)
    }

    private suspend fun populateCoinIds() {
        populateCoinIdsUseCase.invoke(PopulateCoinIdsUseCase.Params(currentTime = Calendar.getInstance()))
    }

    fun showSnackBar(
        msg: String,
        actionLabel: String,
        actionCallback: () -> Unit,
        shouldShowIndefinite: Boolean
    ) {
        _sharedViewState.value = _sharedViewState.value.copy(
            appSnackBar = AppSnackBar(
                shouldShowSnackBar = true,
                snackBarMessage = msg,
                actionLabel = actionLabel,
                actionCallback = actionCallback,
                shouldShowIndefinite = shouldShowIndefinite
            )
        )
    }



    fun hideSnackBar() {
        _sharedViewState.value = _sharedViewState.value.copy(
            appSnackBar = _sharedViewState.value.appSnackBar.copy(shouldShowSnackBar = false)
        )
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

    // nav arguments - where we don't need our app to react to any real time changes
    lateinit var selectedCoinUI: CoinUI
    var webViewUrl: String = ""

    private fun printCurrentState() {
        Log.v("SharedViewModel", "Current SharedViewState = "+ _sharedViewState.value)
    }

    fun onDestinationChanged(route: String?) {
        toolBarHelper.handleToolBarVisibility(route)
        attemptToShowInterstitialAd()
    }

    private fun attemptToShowInterstitialAd() {
        Log.d(TAG, "attemptToShowInterstitialAd called")
        callFunctionWithProbability(probability = 0.05) {
            Log.d(TAG, "shouldShowInterstitialAd = true")
            _sharedViewState.value = _sharedViewState.value.copy(
                shouldShowInterstitialAd = true
            )
        }
    }

    fun showInterstitialAdAttempted() {
        _sharedViewState.value = _sharedViewState.value.copy(
            shouldShowInterstitialAd = false
        )
    }

    // 0.2 probability is a 20% chance of calling the supplied function.
    // 0.5 probability is a 50% chance of calling the supplied function
    private fun callFunctionWithProbability(probability: Double, function: () -> Unit) {
        val randomValue = Random.nextDouble(0.0, 1.0)
        if (randomValue <= probability) {
           function()
        }
    }

}