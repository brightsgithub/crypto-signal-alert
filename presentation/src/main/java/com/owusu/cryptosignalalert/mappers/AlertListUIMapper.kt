package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.AlertItemDomain
import com.owusu.cryptosignalalert.domain.models.AlertListDomainWrapper
import com.owusu.cryptosignalalert.models.AlertItemUI
import com.owusu.cryptosignalalert.models.AlertListUIWrapper

class AlertListUIMapper: UIMapper<AlertListDomainWrapper, AlertListUIWrapper> {

    override fun mapDomainToUI(domainObj: AlertListDomainWrapper): AlertListUIWrapper {
        val alertListDomain = domainObj.alertList
        val list = arrayListOf<AlertItemUI>()
        for(alertItemDomain in alertListDomain) {
            alertItemDomain.apply {
                list.add(
                    AlertItemUI(
                        symbol,
                        cryptoName,
                        frequency,
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

    override fun mapUIToDomain(uiObj: AlertListUIWrapper): AlertListDomainWrapper {
        val alertListUI = uiObj.alertList
        val list = arrayListOf<AlertItemDomain>()
        for (alertItemUI in alertListUI) {
            alertItemUI.apply {
                list.add(
                    AlertItemDomain(
                        symbol,
                        cryptoName,
                        frequency,
                        currentPrice,
                        alertPrice,
                        progress,
                        hasAlertBeenTriggered
                    )
                )
            }
        }
        return AlertListDomainWrapper(list)
    }
}