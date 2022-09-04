package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

class MergeOldPriceTargetWithNewDataUseCase: SuspendedUseCase<
        MergeOldPriceTargetWithNewDataUseCase.Params, List<PriceTargetDomain>> {

    override suspend fun invoke(params: Params): List<PriceTargetDomain> {
        params.apply {
            return getUpdatedPriceTargets(coinsList, priceTargets, lastUpdated)
        }
    }

    private fun getUpdatedPriceTargets(
        coinsList: List<CoinDomain>,
        priceTargets: List<PriceTargetDomain>,
        lastUpdated: String): List<PriceTargetDomain> {

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
                    lastUpdated
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
        hasPriceTargetBeenHit: Boolean,
        lastUpdated: String
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

    data class Params(val coinsList: List<CoinDomain>,
                      val priceTargets: List<PriceTargetDomain>,
                      val lastUpdated: String)

}