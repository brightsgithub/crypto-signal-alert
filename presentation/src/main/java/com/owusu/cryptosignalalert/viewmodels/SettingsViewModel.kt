package com.owusu.cryptosignalalert.viewmodels

import androidx.lifecycle.viewModelScope
import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.usecase.DisableSirenSettingUseCase
import com.owusu.cryptosignalalert.domain.usecase.EnableSirenSettingUseCase
import com.owusu.cryptosignalalert.domain.usecase.IsSirenEnabledUseCase
import com.owusu.cryptosignalalert.domain.usecase.LoadSettingsUseCase
import com.owusu.cryptosignalalert.mappers.SettingDomainToUIMapper
import com.owusu.cryptosignalalert.viewmodels.udf.UdfViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.settings.SettingTypeUI
import com.owusu.cryptosignalalert.viewmodels.udf.settings.SettingsUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.settings.SettingsUdfEvent
import com.owusu.cryptosignalalert.viewmodels.udf.settings.SettingsViewUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val settingDomainToUIMapper: SettingDomainToUIMapper,
    private val isSirenEnabledUseCase: IsSirenEnabledUseCase,
    private val enableSirenSettingUseCase: EnableSirenSettingUseCase,
    private val disableSirenSettingUseCase: DisableSirenSettingUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
): UdfViewModel<SettingsUdfEvent, SettingsViewUiState, SettingsUdfAction>(
    initialUiState = SettingsViewUiState()
) {

    fun loadSettings() {
        viewModelScope.launch(dispatcherBackground) {
            val settings = loadSettingsUseCase.invoke()
            displaySettings(settings)
        }
    }

    private suspend fun displaySettings(settings: List<SettingDomain>) {
        val settingsUI = settingDomainToUIMapper.toUI(settings)

        setUiState {
            copy(settings = settingsUI)
        }


//        withContext(dispatcherMain) {
//            _state.value = _state.value.copy(settings = settingsUI)
//        }
    }

    private fun toggle() {
        if (isSirenEnabled()) {
            disableSirenSettingUseCase.execute()
            updateSirenUIState()
        } else {
            enableSirenSettingUseCase.execute()
            updateSirenUIState()
        }
    }

    private fun isSirenEnabled(): Boolean {
        return isSirenEnabledUseCase.execute()
    }
    private fun updateSirenUIState() {
        val isSirenEnabled = isSirenEnabled()
        // Create a new list with updated settings
        val updatedSettings = uiState.value.settings.map { setting ->
            if (setting.settingTypeUI == SettingTypeUI.Siren) {
                setting.copy(
                    selectedValue = "Siren is " + if (isSirenEnabled) "enabled" else "disabled"
                )
            } else {
                setting
            }
        }

        // Update the UI state with the new settings list
        setUiState {
            copy(settings = updatedSettings)
        }
    }

    override fun handleEvent(event: SettingsUdfEvent) {
        when(event) {
            is SettingsUdfEvent.ContactDeveloper -> sendAction { SettingsUdfAction.ActionContactDeveloper }
            SettingsUdfEvent.Nothing -> sendAction { SettingsUdfAction.ActionNothing }
            SettingsUdfEvent.PrivacyPolicy -> {
                sendAction { SettingsUdfAction.ActionNavigateToWebView("https://sites.google.com/view/crypto-price-target-alerts-pri")}
            }
            SettingsUdfEvent.RateTheApp -> sendAction { SettingsUdfAction.ActionOpenGooglePlayStore }
            SettingsUdfEvent.ShareApp -> sendAction { SettingsUdfAction.ActionShareApp }
            SettingsUdfEvent.ToggleSiren -> toggle()
            else -> { }
        }
    }
}