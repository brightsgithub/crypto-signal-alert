package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.AlertListDomainWrapper
import com.owusu.cryptosignalalert.domain.repository.AlertListRepository

class GetPriceTargetsUseCase(
    private val alertListRepository: AlertListRepository):
    SuspendedUseCaseUnit<AlertListDomainWrapper> {

    override suspend fun invoke(): AlertListDomainWrapper {
        return alertListRepository.getAlertList()
    }
}