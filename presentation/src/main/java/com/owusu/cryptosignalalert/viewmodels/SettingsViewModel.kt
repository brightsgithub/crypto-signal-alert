package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.usecase.LoadSettingsUseCase
import com.owusu.cryptosignalalert.mappers.SettingDomainToUIMapper
import com.owusu.cryptosignalalert.models.SettingsViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val settingDomainToUIMapper: SettingDomainToUIMapper,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(SettingsViewState()) // for emitting
    val viewState: Flow<SettingsViewState> = _state // for clients to listen to

    fun loadSettings() {
        viewModelScope.launch(dispatcherBackground) {
            val settings = loadSettingsUseCase.invoke()
            displaySettings(settings)
        }
    }

    private suspend fun displaySettings(settings: List<SettingDomain>) {
        val settingsUI = settingDomainToUIMapper.toUI(settings)
        withContext(dispatcherMain) {
            _state.value = _state.value.copy(settings = settingsUI)
        }
    }
}