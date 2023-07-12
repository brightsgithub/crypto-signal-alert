package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.models.SettingType
import com.owusu.cryptosignalalert.models.SettingTypeUI
import com.owusu.cryptosignalalert.models.SettingUI

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
            isLastSetting = settingDomain.isLastSetting
        )
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
            else -> {
                SettingTypeUI.Nothing
            }
        }
    }

    private fun getSettingTypeDomain(settingUI: SettingUI): SettingType {
        return when (settingUI.settingTypeUI) {
            SettingTypeUI.ContactDeveloper -> SettingType.ContactDeveloper
            SettingTypeUI.PrivacyPolicy -> SettingType.PrivacyPolicy
            SettingTypeUI.RateTheApp -> SettingType.RateTheApp
            SettingTypeUI.ShareApp -> SettingType.ShareApp
            else -> {
                SettingType.Nothing
            }
        }
    }
}
