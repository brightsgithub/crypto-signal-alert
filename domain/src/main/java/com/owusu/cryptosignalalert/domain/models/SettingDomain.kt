package com.owusu.cryptosignalalert.domain.models

data class SettingDomain(
    val title: String,
    val subTitle: String? = null,
    val selectedValue: String? = null,
    val isFirstSetting: Boolean = false,
    val isLastSetting: Boolean = false
)