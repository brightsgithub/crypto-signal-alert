package com.owusu.cryptosignalalert.viewmodels.udf.settings

import android.os.Parcelable
import com.owusu.cryptosignalalert.viewmodels.udf.UdfUiState
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingUI(
    val settingTypeUI: @RawValue SettingTypeUI = SettingTypeUI.Nothing,
    val title: String,
    val subTitle: String? = null,
    val selectedValue: String? = null,
    val isFirstSetting: Boolean = false,
    val isLastSetting: Boolean = false,
    val iconId: Int? = null
    ): UdfUiState, Parcelable

sealed class SettingTypeUI {
    object PrivacyPolicy: SettingTypeUI()
    object ShareApp: SettingTypeUI()
    object ContactDeveloper: SettingTypeUI()
    object RateTheApp: SettingTypeUI()
    object Nothing: SettingTypeUI()
    object DonateBTC: SettingTypeUI()
    object DonateETH: SettingTypeUI()
    object Siren: SettingTypeUI()
}