package com.owusu.cryptosignalalert.data.datasource.settings

import android.content.Context
import com.owusu.cryptosignalalert.data.datasource.AppPreferences
import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.models.SettingType
import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository

class LocalFixedSettingsDataSource(
    private val context: Context,
    private val settingsUtils: SettingsUtils,
    private val appPreferences: AppPreferencesRepository
    ): SettingsDataSource {
    override suspend fun getSettings(): List<SettingDomain> {
        return listOf(
            SettingDomain(
                settingType = SettingType.ContactDeveloper,
                isFirstSetting = true,
                title = "Contact Developer",
                subTitle = "Contact for support"
            ),
            SettingDomain(
                settingType = SettingType.ShareApp,
                title = "Share App",
                subTitle = "Share this app with others"
            ),
            SettingDomain(
                settingType = SettingType.PrivacyPolicy,
                title = "Privacy Policy",
                subTitle = "Details of this app policies",
                // selectedValue = "Version 1.0.0"
            ),
            SettingDomain(
                settingType = SettingType.RateTheApp,
                title = "Rate App",
                subTitle = "Add a review to the Google Play Store",
            ),
            SettingDomain(
                settingType = SettingType.Siren,
                title = "Siren",
                subTitle = "When enabled, a siren sound will play when price target has been met",
                selectedValue = "Siren is " + if (appPreferences.isSirenEnabled()) {"enabled"} else {"disabled"}
            ),
            SettingDomain(
                title = "App Version",
                subTitle = getAppVersion(),
                //selectedValue = "Version 1.0.0"
            ),
            SettingDomain(
                settingType = SettingType.DonateBTC,
                title = "Donate BTC",
                subTitle = "Please support development effort"
            ),
            SettingDomain(
                settingType = SettingType.DonateETH,
                title = "Donate ETH/ERC20",
                subTitle = "Please support development effort"
            )
        )
    }

    private fun getAppVersion(): String {
        return settingsUtils.getAppVersionName(context)
    }
}