package com.owusu.cryptosignalalert.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.states.PurchasedStateDomain
import com.owusu.cryptosignalalert.domain.models.states.StartUpBillingState
import com.owusu.cryptosignalalert.domain.usecase.PopulateCoinIdsUseCase
import com.owusu.cryptosignalalert.domain.usecase.RefreshSkuDetailsUseCase
import com.owusu.cryptosignalalert.domain.usecase.SavedPurchasedStateChangesUseCase
import com.owusu.cryptosignalalert.domain.usecase.StartupBillingUseCase
import com.owusu.cryptosignalalert.viewmodels.udf.home.AppSnackBar
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.udf.home.SharedViewState
import com.owusu.cryptosignalalert.resource.AppStringProvider
import com.owusu.cryptosignalalert.viewmodels.helpers.ToolBarHelper
import com.owusu.cryptosignalalert.viewmodels.udf.UdfViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.home.HomeUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.home.HomeUdfEvent
import com.owusu.cryptosignalalert.views.screens.TAG
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class SharedViewModel(
    private val startupBillingUseCase: StartupBillingUseCase,
    private val populateCoinIdsUseCase: PopulateCoinIdsUseCase,
    private val savedPurchasedStateChangesUseCase: SavedPurchasedStateChangesUseCase,
    private val refreshSkuDetailsUseCase: RefreshSkuDetailsUseCase,
    private val appStringProvider: AppStringProvider,
    private val toolBarHelper: ToolBarHelper
    ): UdfViewModel<HomeUdfEvent, SharedViewState, HomeUdfAction>(initialUiState = SharedViewState()) {

//    private val _sharedViewState = MutableStateFlow(SharedViewState()) // for emitting
//    val sharedViewState: Flow<SharedViewState> = _sharedViewState // for clients to listen to

    init {
        toolBarHelper.initToolBarHelper(uiState, ::setTheUiState)
        startCollectingPurchasedState()
        startCollectingBillingState()
    }

    fun initApp() {
        refreshBillingState()
        populateCoinIds()
    }

    private fun startCollectingPurchasedState() {
        viewModelScope.launch {
            Log.d("SharedViewModel", "calling startCollectingPurchasedState")
            savedPurchasedStateChangesUseCase.invoke().collect { purchasedStateDomain ->
                updatePurchasedState(purchasedStateDomain)
                printCurrentState()
            }
        }
    }

    /**
     * Do what we need to do in terms of show hide based on the state of iap.
     */
    private fun startCollectingBillingState() {
        viewModelScope.launch {
            Log.d("SharedViewModel", "calling startCollectingBillingState")
            startupBillingUseCase.invoke(this).collect { startUpBillingState ->
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
    }

    private fun refreshBillingState() {
        viewModelScope.launch {
            Log.d("SharedViewModel", "calling refreshBillingState")
            refreshSkuDetailsUseCase.invoke()
        }
    }

    private fun updatePurchasedState(purchasedStateDomain: PurchasedStateDomain) {
        Log.d("SharedViewModel", "updatePurchasedState1 = "+ purchasedStateDomain)
        val newState = uiState.value.copy(
            purchasedState = uiState.value.purchasedState.copy(
                isAppFree = purchasedStateDomain.isAppFree,
                isAdsPurchased = purchasedStateDomain.isAdsPurchased,
                isPriceTargetLimitPurchased = purchasedStateDomain.isPriceTargetLimitPurchased
            )
        )
        setTheUiState(newState)

        Log.d("SharedViewModel", "updatePurchasedState2 = "+ uiState.value)
    }

    private fun populateCoinIds() {
        viewModelScope.launch {
            populateCoinIdsUseCase.invoke(PopulateCoinIdsUseCase.Params(currentTime = Calendar.getInstance()))
        }
    }

    private fun showSnackBar(
        msg: String,
        actionLabel: String,
        actionCallback: () -> Unit,
        shouldShowIndefinite: Boolean
    ) {
        val newState = uiState.value.copy(
            appSnackBar = AppSnackBar(
                shouldShowSnackBar = true,
                snackBarMessage = msg,
                actionLabel = actionLabel,
                actionCallback = actionCallback,
                shouldShowIndefinite = shouldShowIndefinite
            )
        )
        setTheUiState(newState)
    }

    fun hideSnackBar() {
        val newState = uiState.value.copy(
            appSnackBar = uiState.value.appSnackBar.copy(shouldShowSnackBar = false)
        )
        setTheUiState(newState)
    }

    // nav arguments - where we don't need our app to react to any real time changes
    lateinit var selectedCoinUI: CoinUI
    var webViewUrl: String = ""

    private fun printCurrentState() {
        Log.v("SharedViewModel", "Current SharedViewState = "+ uiState.value)
    }

    fun onDestinationChanged(route: String?) {
        toolBarHelper.handleToolBarVisibility(route)
        //attemptToShowInterstitialAd()
    }

    private fun attemptToShowInterstitialAd() {
        Log.d(TAG, "attemptToShowInterstitialAd called")
        callFunctionWithProbability(probability = 0.1) {
            Log.d(TAG, "shouldShowInterstitialAd = true")
            val newState = uiState.value.copy(
                shouldShowInterstitialAd = true
            )
            setTheUiState(newState)
        }
    }

    fun showInterstitialAdAttempted() {
        val newState = uiState.value.copy(
            shouldShowInterstitialAd = false
        )
        setTheUiState(newState)
    }

    // 0.2 probability is a 20% chance of calling the supplied function.
    // 0.5 probability is a 50% chance of calling the supplied function
    private fun callFunctionWithProbability(probability: Double, function: () -> Unit) {
        val randomValue = Random.nextDouble(0.0, 1.0)
        if (randomValue <= probability) {
           function()
        }
    }

    private fun setTheUiState(uiState: SharedViewState) {
        setUiState {
            uiState
        }
    }

    override fun handleEvent(event: HomeUdfEvent) {
        println("Testing_udf_HomeUdfEvent_SharedViewModel action $event")
        when (event) {
            is HomeUdfEvent.OnCoinRowClicked -> {
                selectedCoinUI = event.selectedCoinUI
                sendAction { HomeUdfAction.NavigateToPriceTargetEntry }
            }
            is HomeUdfEvent.OnSearchBarClicked -> sendAction { HomeUdfAction.NavigateToSearch }
            is HomeUdfEvent.OnSettingsClicked -> sendAction { HomeUdfAction.NavigateToSettings }
            is HomeUdfEvent.ShowSnackBar -> {
                event.apply {
                    showSnackBar(
                        actionCallback = actionCallback,
                        actionLabel = actionLabel,
                        msg = msg,
                        shouldShowIndefinite = shouldShowIndefinite
                    )
                }
            }
            is HomeUdfEvent.NavigateToPriceTargets -> sendAction { HomeUdfAction.NavigateToPriceTargets }
            is HomeUdfEvent.NavigateToPurchase -> sendAction { HomeUdfAction.NavigateToPurchase }
            is HomeUdfEvent.NavigateToPriceTargetEntryFromSearch -> {
                selectedCoinUI = event.selectedCoinUI
                sendAction { HomeUdfAction.NavigateToPriceTargetEntryFromSearch }
            }
            is HomeUdfEvent.NavigateToWebView -> {
                webViewUrl = event.webViewUrl
                sendAction { HomeUdfAction.NavigateToWebView(webViewUrl) }
            }
            else -> {

            }
        }
    }
}