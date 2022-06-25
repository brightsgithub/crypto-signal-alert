package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.AlertListDomainWrapper

interface AlertListRepository {
    suspend fun getAlertList(): AlertListDomainWrapper
}