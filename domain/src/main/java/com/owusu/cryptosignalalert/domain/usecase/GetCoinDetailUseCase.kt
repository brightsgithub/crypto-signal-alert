package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDetailDomain
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class GetCoinDetailUseCase(private val coinsRepository: CoinsRepository):
    SuspendedUseCase<GetCoinDetailUseCase.Params, CoinDetailDomain> {

    override suspend fun invoke(params: Params): CoinDetailDomain {
        return coinsRepository.getCoinDetail(params.coinId)
    }

    data class Params(val coinId: String)
}

