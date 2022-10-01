package com.owusu.cryptosignalalert.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsUseCase
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.PriceTargetUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertListViewModel(
    private val priceTargetsMapper: UIMapper<PriceTargetDomain, PriceTargetUI>,
    private val getPriceTargetsUseCase: GetPriceTargetsUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher,
    private val workerTag: String,
    private val app: Application): AndroidViewModel(app) {

    var workInfoLiveData: LiveData<List<WorkInfo>> // <-- ADD THIS
    private val workManager: WorkManager = WorkManager.getInstance(app)

    private val _state = MutableSharedFlow<AlertListViewState>() // for emitting
    val viewState: Flow<AlertListViewState> = _state // for clients to listen to

    // WorkManager in AndroidViewModel https://developer.android.com/codelabs/android-adv-workmanager#3
    init {
        // we won't observe here since we don't want to pass in the lifecycleScope.
        // let the view observe and let it call back the viewmodel.
        workInfoLiveData = workManager.getWorkInfosForUniqueWorkLiveData(workerTag)
    }

    fun loadAlertList(scope: CoroutineScope = viewModelScope) {
        scope.launch(dispatcherBackground) {
            showLoadingState()
            val alertList = getPriceTargetsUseCase.invoke()
            handleAlertList(alertList)
        }
    }

    private suspend fun showLoadingState() {
        withContext(dispatcherMain) {
            _state.emit(AlertListViewState.ShowLoadingState)
        }
    }

    private suspend fun hideLoadingState() {
        withContext(dispatcherMain) {
            _state.emit(AlertListViewState.HideLoadingState)
        }
    }

    private suspend fun handleAlertList(priceTargetsDomainList: List<PriceTargetDomain>) {
        withContext(dispatcherMain) {
            val alertListUIWrapper = priceTargetsMapper.mapDomainListToUIList(priceTargetsDomainList)
            _state.emit(AlertListViewState.AlertListDataSuccess(alertListUIWrapper))
        }
    }
}