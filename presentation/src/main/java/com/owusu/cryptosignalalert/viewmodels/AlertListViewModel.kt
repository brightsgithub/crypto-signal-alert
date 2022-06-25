package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.models.AlertListViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class AlertListViewModel: ViewModel() {

    private val _state = MutableSharedFlow<AlertListViewState>() // for emitting
    val viewState: Flow<AlertListViewState> = _state // for clients to listen to


    fun loadAlertList(scope: CoroutineScope = viewModelScope) {

    }

}