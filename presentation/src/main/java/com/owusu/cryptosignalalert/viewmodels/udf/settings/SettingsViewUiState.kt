package com.owusu.cryptosignalalert.viewmodels.udf.settings

import com.owusu.cryptosignalalert.viewmodels.udf.UdfUiState

data class SettingsViewUiState(
    val settings: List<SettingUI> = emptyList()
) : UdfUiState