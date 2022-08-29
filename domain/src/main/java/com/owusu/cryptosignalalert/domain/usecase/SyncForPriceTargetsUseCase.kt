package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.utils.DateUtils

class SyncForPriceTargetsUseCase(
    private val getPriceTargetsUseCase: GetPriceTargetsUseCase,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val updatePriceTargetsUseCase: UpdatePriceTargetsUseCase,
    private val dateUtil: DateUtils
    ): SuspendedUseCaseUnit<Boolean> {

    // needs to have a flow so that the Alarm manager can listen
    // and the calling view model can listen
    override suspend fun invoke(): Boolean {

        // 1. getPriceTargetsUseCase which will give you a list of price targets.
        // need to get the price targets that have not been hit
        val priceTargets = getPriceTargets()
        if (priceTargets.isEmpty()) return false

        val ids = getListOfIds(priceTargets)

        // 2. Query against the api for targets obtained above.
        val coinsList = getCoinsList(ids)

        // 3. carry out business rules so we know what targets have been met etc. and we know
        // what fields to update in the DB via savePriceTargetUseCase
        val updatedPriceTargets = getUpdatedPriceTargets(coinsList, priceTargets)

        // 4. update all price targets. At this point some may have met their price targets
        // and some will just have updated their current price updated
        updatePriceTargets(updatedPriceTargets)
        return updatedPriceTargets.isNotEmpty()
    }

    private suspend fun updatePriceTargets(updatedPriceTargets: List<PriceTargetDomain>) {
        updatePriceTargetsUseCase.invoke(UpdatePriceTargetsUseCase.Params(updatedPriceTargets))
    }

    private fun getUpdatedPriceTargets(
        coinsList: List<CoinDomain>,
        priceTargets: List<PriceTargetDomain>): List<PriceTargetDomain> {

        // convert list to map more efficient if we search across a map than O(n.size of array) array
        val searchableMapOfCoins = mutableMapOf<String, CoinDomain>()
        coinsList.associateByTo(searchableMapOfCoins) {it.id}

        // New updated list
        val updatedPriceTargetDomainList = arrayListOf<PriceTargetDomain>()
        priceTargets.forEach {

            val coinDomain = searchableMapOfCoins[it.id]

            if (
                coinDomain?.currentPrice != null
                && it.userPriceTarget != null
                && it.priceTargetDirection != PriceTargetDirection.NOT_SET
            ) {

                // keep track of our current values
                val hasUserBeenAlerted = it.hasUserBeenAlerted
                val userPriceTarget = it.userPriceTarget
                val priceTargetDirection = it.priceTargetDirection

                // has the target been hit
                val hasPriceTargetBeenHit = if (!it.hasPriceTargetBeenHit) {
                    checkIfPriceTargetBeenHit(
                        coinDomain.currentPrice,
                        it.userPriceTarget,
                        it.priceTargetDirection
                    )
                } else {
                    true // it has already been hit, so keep the same value
                }

                // TODO - also update the progress to show how close we are

                // Convert the latest price with our updated PriceTargetDomain
                val updatedPriceTargetDomain = getUpdatedPriceTargetDomain(
                    coinDomain,
                    hasUserBeenAlerted,
                    userPriceTarget,
                    priceTargetDirection,
                    hasPriceTargetBeenHit,
                )

                updatedPriceTargetDomainList.add(updatedPriceTargetDomain)
            }

        }
        return updatedPriceTargetDomainList
    }

    private fun getUpdatedPriceTargetDomain(
        coinDomain: CoinDomain,
        hasUserBeenAlerted: Boolean,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection,
        hasPriceTargetBeenHit: Boolean

    ): PriceTargetDomain {
        coinDomain.apply {
            return PriceTargetDomain(
                id = id,
                ath = ath,
                athChangePercentage = athChangePercentage,
                athDate = athDate,
                atl = atl,
                atlChangePercentage = atlChangePercentage,
                atlDate = atlDate,
                circulatingSupply = circulatingSupply,
                currentPrice = currentPrice,
                fullyDilutedValuation = fullyDilutedValuation,
                high24h = high24h,
                image = image,
                lastUpdated = lastUpdated,
                low24h = low24h,
                marketCap = marketCap,
                marketCapChange24h = marketCapChange24h,
                marketCapChangePercentage24h = marketCapChangePercentage24h,
                marketCapRank = marketCapRank,
                maxSupply = maxSupply,
                name = name,
                priceChange24h = priceChange24h,
                priceChangePercentage24h = priceChangePercentage24h,
                symbol = symbol,
                totalSupply = totalSupply,
                totalVolume = totalVolume,
                userPriceTarget = userPriceTarget,
                hasPriceTargetBeenHit = hasPriceTargetBeenHit,
                hasUserBeenAlerted = hasUserBeenAlerted,
                priceTargetDirection = priceTargetDirection
            )
        }

    }

    /**
     * We cannot check if the price has hit a target outside an interval of 5 mins. API restrictions
     * The highest frequency is 5 mins. If we set our price target of 10k at 10:00 and our next check
     * for price is scheduled at 10:05, if the price in the meantime dips below to 9.8k and then back up
     * to 10.1k before our next check at 10:05, we will never know.
     *
     * This method will simply check at the interval of 5 mins, if the price target has been hit at that time.
     */
    private fun checkIfPriceTargetBeenHit(
        currentPrice: Double,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection): Boolean {

        return when (priceTargetDirection) {
            PriceTargetDirection.ABOVE -> { currentPrice >= userPriceTarget }
            PriceTargetDirection.BELOW -> { currentPrice <= userPriceTarget }
            else -> { false }
        }
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