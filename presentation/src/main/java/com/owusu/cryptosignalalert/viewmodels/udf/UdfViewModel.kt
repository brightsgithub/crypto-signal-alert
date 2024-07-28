package com.owusu.cryptosignalalert.viewmodels.udf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
    The class intends to formalise and provide a common definition for the Unidirectional Data Flow UI pattern,
    to ease maintenance and avoid individual styling in the ViewModels.
    See: https://developer.android.com/topic/architecture/ui-layer#why-use-udf
 */
abstract class UdfViewModel<E : UdfEvent, S : UdfUiState, A : UdfAction>(val initialUiState: S) : ViewModel() {

    private val _uiState: MutableStateFlow<S> = MutableStateFlow(initialUiState)
    val uiState by lazy { _uiState.asStateFlow() }

    private val _action: Channel<A> = Channel()
    val action by lazy { _action.receiveAsFlow() }

    abstract fun handleEvent(event: E)

    protected fun setUiState(reduce: S.() -> S) {
        _uiState.update { _uiState.value.reduce() }
    }

    protected fun sendAction(builder: () -> A) {
        val effectValue = builder()
        viewModelScope.launch {
            _action.send(effectValue)
        }
    }
}