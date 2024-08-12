package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.states.UpdateSyncState
import com.owusu.cryptosignalalert.domain.usecase.DeletePriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.IsSyncRunningUseCase
import com.owusu.cryptosignalalert.domain.usecase.ListenToSyncPriceTargetsUpdatesUseCase
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.viewmodels.udf.pricetargetlist.AlertListViewState
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.pricetargetlist.PriceTargetListUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.pricetargetlist.PriceTargetListUdfEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AlertListViewModel(
    private val priceTargetsMapper: UIMapper<PriceTargetDomain, PriceTargetUI>,
    private val getPriceTargetsUseCase: GetPriceTargetsUseCase,
    private val deletePriceTargetsUseCase: DeletePriceTargetsUseCase,
    private val isSyncRunningUseCase: IsSyncRunningUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val listenToSyncPriceTargetsUpdatesUseCase: ListenToSyncPriceTargetsUpdatesUseCase
): UdfViewModel<PriceTargetListUdfEvent, AlertListViewState, PriceTargetListUdfAction>(initialUiState = AlertListViewState()) {

    fun isSyncRunning(): Boolean {
        return isSyncRunningUseCase.execute()
    }

    private fun init() {
        loadAlertList()
        listenToUpdateSyncState()
    }
    private fun loadAlertList(scope: CoroutineScope = viewModelScope) {
        scope.launch(dispatcherBackground) {
            showLoadingState()
            getPriceTargetsUseCase.invoke().collect { alertList ->
                handleAlertList(alertList)
            }
        }
    }

    private fun listenToUpdateSyncState(scope: CoroutineScope = viewModelScope) {
        scope.launch(dispatcherBackground) {
            listenToSyncPriceTargetsUpdatesUseCase.invoke().collect {
                showRemainingSyncWorkToBeDone(it)
            }
        }
    }

    private fun showRemainingSyncWorkToBeDone(updateSyncState: UpdateSyncState) {
        val shouldShowSyncState = (updateSyncState.remainingPercentageOfWorkToBeDone > 0 && updateSyncState.remainingPercentageOfWorkToBeDone < 1)
        val newUiState = uiState.value.copy(
            remainingSyncPercentageToBeUpdated = updateSyncState.remainingPercentageOfWorkToBeDone,
            shouldShowSyncState = shouldShowSyncState
        )
        setUiState { newUiState }
    }

    private fun deletePriceTarget(priceTargetUI: PriceTargetUI) {
        viewModelScope.launch(dispatcherBackground) {
            val targets = priceTargetsMapper.mapUIListToDomainList(listOf(priceTargetUI))
            deletePriceTargetsUseCase.invoke(DeletePriceTargetsUseCase.Params(targets))
        }
    }

    private fun showLoadingState() {
        val newUiState = uiState.value.copy(isLoading = true)
        setUiState { newUiState }
    }

    private fun hideLoadingState() {
        val newUiState = uiState.value.copy(isLoading = false)
        setUiState { newUiState }
    }

    private fun handleAlertList(priceTargetsDomainList: List<PriceTargetDomain>) {
        val priceTargets = priceTargetsMapper.mapDomainListToUIList(priceTargetsDomainList)
        val numOfTargetsMet = calculateNumberOfTargetsMet(priceTargets)
        val newUiState = uiState.value.copy(
            priceTargets = priceTargets,
            numberOfTargetsMet = numOfTargetsMet,
            totalNumberOfTargets = priceTargets.size
        )
        setUiState { newUiState }
    }

    private fun calculateNumberOfTargetsMet(priceTargetsList: List<PriceTargetUI>): Int {
        var numOfTargetsMet = 0;
        priceTargetsList.forEach {
            if (it.hasPriceTargetBeenHit) {
                numOfTargetsMet++
            }
        }
        return numOfTargetsMet
    }

    override fun handleEvent(event: PriceTargetListUdfEvent) {
        when (event) {
            is PriceTargetListUdfEvent.DeletePriceTarget -> {
                deletePriceTarget(event.priceTargetUI)
            }

            is PriceTargetListUdfEvent.Initialize -> {
                init()
            }
        }
    }
}