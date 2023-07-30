package com.owusu.cryptosignalalert.domain.models

data class SettingDomain(
    val settingType: SettingType = SettingType.Nothing,
    val title: String,
    val subTitle: String? = null,
    val selectedValue: String? = null,
    val isFirstSetting: Boolean = false,
    val isLastSetting: Boolean = false
)

sealed class SettingType {
    object PrivacyPolicy: SettingType()
    object ShareApp: SettingType()
    object ContactDeveloper: SettingType()
    object RateTheApp: SettingType()
    object Siren: SettingType()
    object DonateBTC: SettingType()
    object DonateETH: SettingType()
    object Nothing: SettingType()
}