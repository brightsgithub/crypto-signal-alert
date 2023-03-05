package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinIdDomain
import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import kotlinx.coroutines.flow.Flow

class SearchCoinIdsUseCase(
    private val coinsRepository: CoinsRepository
): UseCase<SearchCoinIdsUseCase.Params, Flow<List<CoinIdDomain>>> {

    override fun invoke(params: Params): Flow<List<CoinIdDomain>> {
        return coinsRepository.searchCoinIds(params.searchStr)
    }

    data class Params(val searchStr: String)
}