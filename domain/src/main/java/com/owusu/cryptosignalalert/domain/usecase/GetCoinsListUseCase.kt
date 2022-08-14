package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.Coin
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class GetCoinsListUseCase(
    private val coinsRepository: CoinsRepository
    ): SuspendedUseCase<GetCoinsListUseCase.Params, List<Coin>> {

    override suspend fun invoke(params: Params): List<Coin> {
         params.apply {
             return coinsRepository.getCoinsList(
                 page = page,
                 recordsPerPage = recordsPerPage,
                 currencies = currencies,
                 ids = ids)
        }
    }

    data class Params(val page: Int,
                      val recordsPerPage: Int,
                      val currencies: String,
                      var ids: String? = null)
}