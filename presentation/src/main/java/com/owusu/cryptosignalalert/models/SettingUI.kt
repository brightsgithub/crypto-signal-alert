package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingUI(
    val title: String,
    val subTitle: String? = null,
    val selectedValue: String? = null,
    val isFirstSetting: Boolean = false,
    val isLastSetting: Boolean = false
    ): Parcelable