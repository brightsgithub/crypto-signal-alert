package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.CoinsRepository

class PopulateCoinIdsUseCase(
    private val coinsRepository: CoinsRepository
): SuspendedUseCaseUnit<Unit> {
    override suspend fun invoke() {
        if (!coinsRepository.hasCoinIdsBeenPopulated()) {
            saveNewCoinIds()
        }
    }

    // once the data has been saved, set a DATE in preferences so we don't perform this
    // again after a few months
    private suspend fun checkIfItsTimeToFetchNewCoinIdData() {
        coinsRepository.nukeCoinIdsData()
    }

    private suspend fun saveNewCoinIds() {
        val coinIds = coinsRepository.getAllCoinIds()
        coinsRepository.saveAllCoinIds(coinIds)
    }
}
