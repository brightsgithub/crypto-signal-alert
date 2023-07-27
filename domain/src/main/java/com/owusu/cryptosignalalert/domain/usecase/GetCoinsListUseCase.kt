package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.CoinsListResult
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class GetCoinsListUseCase(
    private val coinsRepository: CoinsRepository
    ): SuspendedUseCase<GetCoinsListUseCase.Params, CoinsListResult> {

    override suspend fun invoke(params: Params): CoinsListResult {
         params.apply {
             return coinsRepository.getCoinsList(
                 page = page,
                 recordsPerPage = recordsPerPage,
                 currencies = currencies,
                 ids = ids)
        }
    }

    data class Params(val page: Int = 1,
                      val recordsPerPage: Int = 1,
                      val currencies: String = "usd",
                      var ids: String? = null)
}