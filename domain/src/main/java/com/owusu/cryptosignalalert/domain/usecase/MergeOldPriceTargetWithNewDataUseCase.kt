package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.*
import java.util.*

class MergeOldPriceTargetWithNewDataUseCase(
    private val getHistoricalPriceUseCase: GetHistoricalPriceUseCase): SuspendedUseCase<
        MergeOldPriceTargetWithNewDataUseCase.Params, List<PriceTargetDomain>> {

    override suspend fun invoke(params: Params): List<PriceTargetDomain> {
        params.apply {
            return getUpdatedPriceTargets(coinsList, priceTargets, lastUpdated)
        }
    }

    private suspend fun getUpdatedPriceTargets(
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
                val localPrimeId = it.localPrimeId
                val hasUserBeenAlerted = it.hasUserBeenAlerted
                val userPriceTarget = it.userPriceTarget
                val priceTargetDirection = it.priceTargetDirection
                var completedOnDate = it.completedOnDate

                // has the target been hit
                val hasPriceTargetBeenHit = if (!it.hasPriceTargetBeenHit) {
                    val hasBeenHit = checkIfPriceTargetBeenHit(
                        coinDomain.currentPrice,
                        it.userPriceTarget,
                        it.priceTargetDirection,
                        coinDomain.id
                    )

                    // if the price target has just been hit, lets store this as our completed date
                    if (hasBeenHit) {
                        completedOnDate = lastUpdated
                    }

                    hasBeenHit
                } else {
                    true // it has already been hit, so keep the same value
                }

                // TODO - also update the progress to show how close we are

                // Convert the latest price with our updated PriceTargetDomain
                val updatedPriceTargetDomain = getUpdatedPriceTargetDomain(
                    localPrimeId,
                    coinDomain,
                    hasUserBeenAlerted,
                    userPriceTarget,
                    priceTargetDirection,
                    hasPriceTargetBeenHit,
                    lastUpdated,
                    completedOnDate
                )

                updatedPriceTargetDomainList.add(updatedPriceTargetDomain)
            }

        }
        return updatedPriceTargetDomainList
    }

    private fun getUpdatedPriceTargetDomain(
        localPrimeId: Int,
        coinDomain: CoinDomain,
        hasUserBeenAlerted: Boolean,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection,
        hasPriceTargetBeenHit: Boolean,
        lastUpdated: String,
        completedOnDate: String?
    ): PriceTargetDomain {
        coinDomain.apply {
            return PriceTargetDomain(
                localPrimeId = localPrimeId,
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
                priceTargetDirection = priceTargetDirection,
                completedOnDate = completedOnDate
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
    private suspend fun checkIfPriceTargetBeenHit(
        currentPrice: Double,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection,
        coinId: String
    ): Boolean {


        val historicalPriceData = getHistoricalPriceUseCase.invoke(GetHistoricalPriceUseCase.Params(coinId))

        val fifteenMinutesAgo = getHistoricalTimestamp(20)
        val filteredHistoricPriceDomains = onlyAddPricesFrom15MinutesAgo(fifteenMinutesAgo, historicalPriceData, currentPrice)


        filteredHistoricPriceDomains.forEach {
            val priceToCheck = it.price
            val hasTargetBeenHit = when (priceTargetDirection) {
                PriceTargetDirection.ABOVE -> { priceToCheck >= userPriceTarget }
                PriceTargetDirection.BELOW -> { priceToCheck <= userPriceTarget }
                else -> { false }
            }

            if (hasTargetBeenHit) {
                return true
            }
        }

        return false
    }

    private fun getHistoricalTimestamp(timeInMinutesToSubtract: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, -timeInMinutesToSubtract)
        return calendar.timeInMillis
    }

    /**
     * Since we check every 15 mins to see if the target has been met, when we get a list of all
     * historic prices (which is returned from the beginning of the day, for the price on a 5 min interval)
     * we only grab the last 15 mins
     */
    private fun onlyAddPricesFrom15MinutesAgo(
        timestamp15MinutesAgo: Long,
        historicalPriceData: HistoricPriceWrapperDomain,
        currentPrice: Double):
            List<HistoricPriceDomain> {

        val filteredHistoricPriceDomains = arrayListOf<HistoricPriceDomain>()

        for(priceApi:  HistoricPriceDomain in historicalPriceData.prices) {
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()

            val previousTimestamp = priceApi.timestamp

            cal1.timeInMillis = previousTimestamp
            cal2.timeInMillis = timestamp15MinutesAgo

            val comparison = cal1.compareTo(cal2)

            // When the previous timestamp is greater or equal to 15 mins ago, add it to our filtered
            // list for later analysis
            if (comparison >= 0) {
                filteredHistoricPriceDomains.add(priceApi)
            }
        }

        // add the current price as a HistoricPriceDomain
        filteredHistoricPriceDomains.add(
            HistoricPriceDomain(
                Calendar.getInstance().timeInMillis,
                currentPrice
            ))

        return filteredHistoricPriceDomains
    }

    data class Params(val coinsList: List<CoinDomain>,
                      val priceTargets: List<PriceTargetDomain>,
                      val lastUpdated: String)
}