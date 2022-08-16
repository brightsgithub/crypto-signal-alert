package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetsWrapper
import com.owusu.cryptosignalalert.domain.utils.DateUtils

class SyncForPriceTargetsUseCase(
    private val getPriceTargetsUseCase: GetPriceTargetsUseCase,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val updatePriceTargetsUseCase: UpdatePriceTargetsUseCase,
    private val dateUtil: DateUtils
    ): SuspendedUseCaseUnit<Unit> {

    // needs to have a flow so that the Alarm manager can listen
    // and the calling view model can listen
    override suspend fun invoke() {

         // 1. getPriceTargetsUseCase which will give you a list of price targets
        val priceTargets = getPriceTargets()
        if (priceTargets.isEmpty()) return

        val ids = getListOfIds(priceTargets)

        // 2. Query against the api for targets obtained above.
        val coinsList = getCoinsList(ids)

        // 3. carry out business rules so we know what targets have been met etc. and we know
        // what fields to update in the DB via savePriceTargetUseCase
        val updatedPriceTargets = getUpdatedPriceTargets(coinsList, priceTargets)

        // 4. update all price targets. At this point some may have met their price targets
        // and some will just have updated their price targets
        updatePriceTargets(updatedPriceTargets)
    }

    private suspend fun updatePriceTargets(updatedPriceTargets: List<PriceTargetDomain>) {
        updatePriceTargetsUseCase.invoke(UpdatePriceTargetsUseCase.Params(updatedPriceTargets))
    }

    private fun getUpdatedPriceTargets(
        coinsList: List<CoinDomain>,
        priceTargets: List<PriceTargetDomain>): List<PriceTargetDomain> {
            TODO()
    }

    private suspend fun getPriceTargets(): List<PriceTargetDomain> {
        return getPriceTargetsUseCase.invoke()
    }

    private fun getListOfIds(priceTargets: List<PriceTargetDomain>): List<String> {
        val ids = arrayListOf<String>()
        priceTargets.map {
            ids.add(requireNotNull(it.id))
        }
        return ids
    }

    private suspend fun getCoinsList(ids: List<String>): List<CoinDomain> {

        val params = GetCoinsListUseCase.Params(
            page = 1,
            recordsPerPage = ids.size,
            ids = ids.joinToString(","),
            currencies = "usd"
        )

        return getCoinsListUseCase.invoke(params)
    }
}