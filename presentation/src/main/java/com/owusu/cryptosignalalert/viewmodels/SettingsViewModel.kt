package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.usecase.DisableSirenSettingUseCase
import com.owusu.cryptosignalalert.domain.usecase.EnableSirenSettingUseCase
import com.owusu.cryptosignalalert.domain.usecase.IsSirenEnabledUseCase
import com.owusu.cryptosignalalert.domain.usecase.LoadSettingsUseCase
import com.owusu.cryptosignalalert.mappers.SettingDomainToUIMapper
import com.owusu.cryptosignalalert.models.SettingTypeUI
import com.owusu.cryptosignalalert.models.SettingsViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val settingDomainToUIMapper: SettingDomainToUIMapper,
    private val isSirenEnabledUseCase: IsSirenEnabledUseCase,
    private val enableSirenSettingUseCase: EnableSirenSettingUseCase,
    private val disableSirenSettingUseCase: DisableSirenSettingUseCase,
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

    fun toggle() {
        if (isSirenEnabled()) {
            disableSirenSetting()
        } else {
            enableSirenSetting()
        }
    }

    private fun enableSirenSetting() {
        enableSirenSettingUseCase.execute()
        updateSirenUIState()
    }

    private fun disableSirenSetting() {
        disableSirenSettingUseCase.execute()
        updateSirenUIState()
    }

    private fun isSirenEnabled(): Boolean {
        return isSirenEnabledUseCase.execute()
    }

    private fun updateSirenUIState() {
        val isSirenEnabled = isSirenEnabled()
        val settings = _state.value.settings.map { setting ->
            if (setting.settingTypeUI == SettingTypeUI.Siren) {
                setting.copy(
                    selectedValue = "Siren is " + if (isSirenEnabled) {"enabled"} else {"disabled"}
                )
            } else {
                setting
            }
        }

        _state.value = _state.value.copy(settings = settings)
    }
}