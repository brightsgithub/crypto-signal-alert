package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.domain.models.AlertFrequency
import com.owusu.cryptosignalalert.domain.models.AlertItemDomain
import com.owusu.cryptosignalalert.domain.models.AlertListDomainWrapper
import com.owusu.cryptosignalalert.domain.repository.AlertListRepository

class AlertListRepositoryImpl : AlertListRepository {
    override suspend fun getAlertList(): AlertListDomainWrapper {
        return getFakeData()
    }

    private fun getFakeData() : AlertListDomainWrapper {
        val alertList: ArrayList<AlertItemDomain> = arrayListOf()
        for (i in 1..5) {
            alertList.add(
                AlertItemDomain(
                    "Symbol_$i",
                "SymbolName_$i",
                    AlertFrequency.Once,
                    "$40,000",
                    "$45,000",
                    (70+i),
                    false
                )
            )
        }
        return AlertListDomainWrapper(alertList)
    }
}