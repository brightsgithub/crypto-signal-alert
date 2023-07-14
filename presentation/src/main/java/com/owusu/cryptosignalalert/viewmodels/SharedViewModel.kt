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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class SharedViewModel(
    private val startupBillingUseCase: StartupBillingUseCase,
    private val populateCoinIdsUseCase: PopulateCoinIdsUseCase,
    private val savedPurchasedStateChangesUseCase: SavedPurchasedStateChangesUseCase
    ): ViewModel() {

    private val _sharedViewState = MutableStateFlow(SharedViewState()) // for emitting
    val sharedViewState: Flow<SharedViewState> = _sharedViewState // for clients to listen to

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

    fun showSnackBar(msg: String, actionLabel: String, actionCallback: () -> Unit) {
        _sharedViewState.value = _sharedViewState.value.copy(
            appSnackBar = AppSnackBar(
                shouldShowSnackBar = true,
                snackBarMessage = msg,
                actionLabel = actionLabel,
                actionCallback = actionCallback
            )
        )
    }

    fun hideSettingsIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSettingsIcon = false
            )
        )
    }

    fun showSettingsIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSettingsIcon = true
            )
        )
    }

    fun showSearchIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSearchIcon = true
            )
        )
    }

    fun hideSearchIcon() {
        _sharedViewState.value = _sharedViewState.value.copy(
            actionButtonState = _sharedViewState.value.actionButtonState.copy(
                shouldShowSearchIcon = false
            )
        )
    }

    fun showAllActionItems() {
        showSearchIcon()
        showSettingsIcon()
    }

    fun hideAllActionItems() {
        hideSearchIcon()
        hideSettingsIcon()
    }

    fun showOnlySettings() {
        showSettingsIcon()
        hideSearchIcon()
    }

    fun hideSnackBar() {
        _sharedViewState.value = SharedViewState()
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
}