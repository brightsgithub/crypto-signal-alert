package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.Coin
import com.owusu.cryptosignalalert.domain.utils.DateUtils

class SyncForPriceTargetsUseCase(
    private val getPriceTargetsUseCase: GetPriceTargetsUseCase,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val savePriceTargetUseCase: SavePriceTargetUseCase,
    private val dateUtil: DateUtils
    ): SuspendedUseCaseUnit<Unit> {

    // needs to have a flow so that the Alarm manager can listen
    // and the calling view model can listen


    override suspend fun invoke() {

         // 1. getPriceTargetsUseCase which will give you a list of price targets
        val ids = arrayListOf("bitcoin","ethereum","ripple","solana")

        // 2. Query against the api for targets obtained above.
        val params = GetCoinsListUseCase.Params(
            page = 1,
            recordsPerPage = ids.size,
            ids = ids.joinToString(","),
            currencies = "usd"
        )
        val coinsList = getCoinsList(params)


        // 3. carry out business rules so we know what targets have been met etc. so we know
        // what fields to update in the DB via savePriceTargetUseCase
    }

    private fun getMatchingPriceTargets(coinsList: List<Coin>) {

    }

    private suspend fun getCoinsList(params: GetCoinsListUseCase.Params): List<Coin> {
        return getCoinsListUseCase.invoke(params)
    }
}