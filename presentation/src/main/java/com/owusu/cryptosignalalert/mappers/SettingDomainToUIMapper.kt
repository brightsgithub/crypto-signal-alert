package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.models.SettingType
import com.owusu.cryptosignalalert.models.SettingTypeUI
import com.owusu.cryptosignalalert.models.SettingUI
import com.owusu.cryptosignalalert.R
object SettingDomainToUIMapper {

    fun toUI(settingDomains: List<SettingDomain>): List<SettingUI> {
        val list = arrayListOf<SettingUI>()
        settingDomains.forEach {
            list.add(toUI(it))
        }
        return list
    }

    fun toUI(settingDomain: SettingDomain): SettingUI {
        return SettingUI(
            settingTypeUI = getSettingTypeUI(settingDomain),
            title = settingDomain.title,
            subTitle = settingDomain.subTitle,
            selectedValue = settingDomain.selectedValue,
            isFirstSetting = settingDomain.isFirstSetting,
            isLastSetting = settingDomain.isLastSetting,
            iconId = getIconId(settingDomain)
        )
    }

    private fun getIconId(settingDomain: SettingDomain): Int? {
        return if (settingDomain.settingType.equals(SettingType.DonateBTC)) {
                R.drawable.btc_icon
        } else if (settingDomain.settingType.equals(SettingType.DonateETH)) {
            R.drawable.eth_icon
        } else {
            null
        }
    }

    fun toDomain(settingUIList: List<SettingUI>): List<SettingDomain> {
        val list = arrayListOf<SettingDomain>()
        settingUIList.forEach {
            list.add(toDomain(it))
        }
        return list
    }

    fun toDomain(settingUI: SettingUI): SettingDomain {
        return SettingDomain(
            settingType = getSettingTypeDomain(settingUI = settingUI),
            title = settingUI.title,
            subTitle = settingUI.subTitle,
            selectedValue = settingUI.selectedValue,
            isFirstSetting = settingUI.isFirstSetting,
            isLastSetting = settingUI.isLastSetting
        )
    }

    private fun getSettingTypeUI(settingDomain: SettingDomain): SettingTypeUI {
        return when (settingDomain.settingType) {
            SettingType.ContactDeveloper -> SettingTypeUI.ContactDeveloper
            SettingType.PrivacyPolicy -> SettingTypeUI.PrivacyPolicy
            SettingType.RateTheApp -> SettingTypeUI.RateTheApp
            SettingType.ShareApp -> SettingTypeUI.ShareApp
            SettingType.DonateBTC -> SettingTypeUI.DonateBTC
            SettingType.DonateETH -> SettingTypeUI.DonateETH
            SettingType.Nothing -> SettingTypeUI.Nothing
            SettingType.Siren -> SettingTypeUI.Siren
        }
    }

    private fun getSettingTypeDomain(settingUI: SettingUI): SettingType {
        return when (settingUI.settingTypeUI) {
            SettingTypeUI.ContactDeveloper -> SettingType.ContactDeveloper
            SettingTypeUI.PrivacyPolicy -> SettingType.PrivacyPolicy
            SettingTypeUI.RateTheApp -> SettingType.RateTheApp
            SettingTypeUI.ShareApp -> SettingType.ShareApp
            SettingTypeUI.DonateBTC -> SettingType.DonateBTC
            SettingTypeUI.DonateETH -> SettingType.DonateETH
            SettingTypeUI.Nothing -> SettingType.Nothing
            SettingTypeUI.Siren -> SettingType.Siren
        }
    }
}
