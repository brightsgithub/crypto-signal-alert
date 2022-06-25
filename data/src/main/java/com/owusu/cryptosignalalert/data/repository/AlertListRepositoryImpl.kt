package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.domain.models.AlertItemDomain
import com.owusu.cryptosignalalert.domain.repository.AlertListRepository

class AlertListRepositoryImpl : AlertListRepository {
    override suspend fun getAlertList(): List<AlertItemDomain> {
        TODO("Not yet implemented")
    }
}