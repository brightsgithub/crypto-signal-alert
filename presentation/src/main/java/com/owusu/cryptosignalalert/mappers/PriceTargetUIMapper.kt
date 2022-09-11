package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.models.PriceTargetUI

class PriceTargetUIMapper:UIListMapper<PriceTargetDomain, PriceTargetUI> {
    override fun mapDomainListToUIList(domainList: List<PriceTargetDomain>): List<PriceTargetUI> {
        val list = arrayListOf<PriceTargetUI>()

        domainList.forEach {
            it.apply {
                list.add(
                    PriceTargetUI(
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
                        priceTargetDirection = mapPriceDirectionToUI(priceTargetDirection)
                    )
                )
            }
        }
        return list
    }

    override fun mapUIListToDomainList(uiList: List<PriceTargetUI>): List<PriceTargetDomain> {
        val list = arrayListOf<PriceTargetDomain>()

        uiList.forEach {
            it.apply {
                list.add(
                    PriceTargetDomain(
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
                        priceTargetDirection = mapPriceDirectionToDomain(priceTargetDirection)

                    )
                )
            }
        }
        return list
    }

    private fun mapPriceDirectionToUI(priceTargetDirDomain: PriceTargetDirection): PriceTargetDirectionUI {
        return when (priceTargetDirDomain) {
            PriceTargetDirection.ABOVE -> PriceTargetDirectionUI.ABOVE
            PriceTargetDirection.BELOW -> PriceTargetDirectionUI.BELOW
            PriceTargetDirection.NOT_SET -> PriceTargetDirectionUI.NOT_SET
        }
    }

    private fun mapPriceDirectionToDomain(priceTargetDirUI: PriceTargetDirectionUI): PriceTargetDirection {
        return when (priceTargetDirUI) {
            PriceTargetDirectionUI.ABOVE -> PriceTargetDirection.ABOVE
            PriceTargetDirectionUI.BELOW -> PriceTargetDirection.BELOW
            PriceTargetDirectionUI.NOT_SET -> PriceTargetDirection.NOT_SET
        }
    }
}

enum class PriceTargetDirectionUI {
    ABOVE, BELOW, NOT_SET
}