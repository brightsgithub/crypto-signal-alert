package com.owusu.cryptosignalalert.viewmodels.udf.settings

import com.owusu.cryptosignalalert.viewmodels.udf.UdfAction

sealed class SettingsUdfAction: UdfAction {
    object ActionContactDeveloper: SettingsUdfAction()
    data class ActionNavigateToWebView(val url: String): SettingsUdfAction()
    object ActionOpenGooglePlayStore: SettingsUdfAction()
    object ActionShareApp: SettingsUdfAction()
    object ActionNothing: SettingsUdfAction()
}