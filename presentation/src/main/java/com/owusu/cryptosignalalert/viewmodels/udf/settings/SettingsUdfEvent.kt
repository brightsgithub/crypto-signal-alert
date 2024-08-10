package com.owusu.cryptosignalalert.viewmodels.udf.settings

import com.owusu.cryptosignalalert.viewmodels.udf.UdfEvent

sealed class SettingsUdfEvent: UdfEvent {
    object PrivacyPolicy: SettingsUdfEvent()
    object ShareApp: SettingsUdfEvent()
    object ContactDeveloper: SettingsUdfEvent()
    object RateTheApp: SettingsUdfEvent()
    object Nothing: SettingsUdfEvent()
    object DonateBTC: SettingsUdfEvent()
    object DonateETH: SettingsUdfEvent()
    object ToggleSiren: SettingsUdfEvent()
}