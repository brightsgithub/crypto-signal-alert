package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.SettingDomain
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
            title = settingUI.title,
            subTitle = settingUI.subTitle,
            selectedValue = settingUI.selectedValue,
            isFirstSetting = settingUI.isFirstSetting,
            isLastSetting = settingUI.isLastSetting
        )
    }
}
