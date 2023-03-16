package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.HistoricPriceWrapperDomain
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class GetHistoricalPriceUseCase(private val coinsRepository: CoinsRepository):
    SuspendedUseCase<GetHistoricalPriceUseCase.Params, HistoricPriceWrapperDomain> {

    override suspend fun invoke(params: Params): HistoricPriceWrapperDomain {
        return coinsRepository.getHistoricalPriceData(params.coinId, params.currency)
    }

    data class Params(val coinId: String, val currency: String = "usd")
}