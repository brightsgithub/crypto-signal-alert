package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingUI(
    val settingTypeUI: @RawValue SettingTypeUI = SettingTypeUI.Nothing,
    val title: String,
    val subTitle: String? = null,
    val selectedValue: String? = null,
    val isFirstSetting: Boolean = false,
    val isLastSetting: Boolean = false
    ): Parcelable

sealed class SettingTypeUI {
    object PrivacyPolicy: SettingTypeUI()
    object ShareApp: SettingTypeUI()
    object ContactDeveloper: SettingTypeUI()
    object RateTheApp: SettingTypeUI()
    object Nothing: SettingTypeUI()
}