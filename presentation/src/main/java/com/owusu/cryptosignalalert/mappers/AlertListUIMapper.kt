package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.AlertFrequency
import com.owusu.cryptosignalalert.domain.models.AlertItemDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetsWrapper
import com.owusu.cryptosignalalert.models.AlertFrequencyUI
import com.owusu.cryptosignalalert.models.AlertItemUI
import com.owusu.cryptosignalalert.models.AlertListUIWrapper

class AlertListUIMapper: UIMapper<PriceTargetsWrapper, AlertListUIWrapper> {

    override fun mapDomainToUI(domainObj: PriceTargetsWrapper): AlertListUIWrapper {
        val alertListDomain = domainObj.alertList
        val list = arrayListOf<AlertItemUI>()
        for(alertItemDomain in alertListDomain) {
            alertItemDomain.apply {
                list.add(
                    AlertItemUI(
                        symbol,
                        cryptoName,
                        mapFrequencyToUI(frequency),
                        currentPrice,
                        alertPrice,
                        progress,
                        hasAlertBeenTriggered
                    )
                )
            }
        }
        return AlertListUIWrapper(list)
    }

    override fun mapUIToDomain(uiObj: AlertListUIWrapper): PriceTargetsWrapper {
        val alertListUI = uiObj.alertList
        val list = arrayListOf<AlertItemDomain>()
        for (alertItemUI in alertListUI) {
            alertItemUI.apply {
                list.add(
                    AlertItemDomain(
                        symbol,
                        cryptoName,
                        mapFrequencyToDomain(frequency),
                        currentPrice,
                        alertPrice,
                        progress,
                        hasAlertBeenTriggered
                    )
                )
            }
        }
        return PriceTargetsWrapper(list)
    }

    private fun mapFrequencyToUI(domainFrequency: AlertFrequency): AlertFrequencyUI {
        return when(domainFrequency) {
            is AlertFrequency.EveryTime -> AlertFrequencyUI.EveryTime
            is AlertFrequency.Once -> AlertFrequencyUI.Once
        }
    }

    private fun mapFrequencyToDomain(uiFrequency: AlertFrequencyUI): AlertFrequency {
        return when(uiFrequency) {
            is AlertFrequencyUI.EveryTime -> AlertFrequency.EveryTime
            is AlertFrequencyUI.Once -> AlertFrequency.Once
        }
    }
}