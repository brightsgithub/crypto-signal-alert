package com.owusu.cryptosignalalert.data.datasource.settings

import com.owusu.cryptosignalalert.domain.models.SettingDomain

class LocalFixedSettingsDataSource: SettingsDataSource {
    override suspend fun getSettings(): List<SettingDomain> {
        return listOf(
            SettingDomain(
                isFirstSetting = true,
                title = "Vibrate",
                subTitle = "Current version",
                selectedValue = "Version 1.0.0",
            ),
            SettingDomain(
                title = "Share with friends",
                //subTitle = "Current version",
                selectedValue = "Share with friends value"
            ),
            SettingDomain(
                title = "Privacy Policy",
                subTitle = "Current version",
                // selectedValue = "Version 1.0.0"
            ),
            SettingDomain(
                isLastSetting = true,
                title = "AppVersion",
                //subTitle = "Current version",
                //selectedValue = "Version 1.0.0"
            )
        )
    }
}