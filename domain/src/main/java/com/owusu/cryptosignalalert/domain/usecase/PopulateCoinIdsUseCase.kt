package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.CoinsRepository
import kotlinx.coroutines.delay
import java.util.*

class PopulateCoinIdsUseCase(
    private val coinsRepository: CoinsRepository
): SuspendedUseCase<PopulateCoinIdsUseCase.Params, Unit> {

    override suspend fun invoke(params: Params) {
        if (isCurrentTimeGreaterThanLastUpdatedForCoinIds(params.currentTime)) {
            if (params.delayInMilliSecs != 0L) {
                delay(params.delayInMilliSecs) // so we don't break our api req/min limit
            }
            saveNewCoinIds()
            saveNewTimeToUpdateCoinIdsDate(params.currentTime, 6)
        }
    }

    private fun isCurrentTimeGreaterThanLastUpdatedForCoinIds(currentTime: Calendar) : Boolean {

        if (coinsRepository.getLastCoinIdUpdate() == 0L) {
            return true // First time so we allow an update
        }

        val currentTimestamp = currentTime
        return currentTimestamp.timeInMillis > coinsRepository.getLastCoinIdUpdate()
    }

    private suspend fun saveNewCoinIds() {
        val coinIds = coinsRepository.getAllCoinIds()
        coinsRepository.saveAllCoinIds(coinIds)
    }

    private fun saveNewTimeToUpdateCoinIdsDate(currentTime: Calendar, hoursInFuture: Int) {
        currentTime.add(Calendar.HOUR_OF_DAY, hoursInFuture)
        coinsRepository.setLastCoinIdUpdate(currentTime.timeInMillis)
    }

    data class Params(val currentTime: Calendar, val delayInMilliSecs: Long = 0L)
}
