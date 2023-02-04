package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

class PriceTargetEntityToPriceTargetDomainMapper: DataAPIListMapper<PriceTargetEntity, PriceTargetDomain> {

    override fun transform(priceTargetEntity: List<PriceTargetEntity>): List<PriceTargetDomain> {
        val domainList = arrayListOf<PriceTargetDomain>()
        priceTargetEntity.map {
            it.apply {
                domainList.add(
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
                        hasPriceTargetBeenHit = hasPriceTargetBeenHit,
                        hasUserBeenAlerted = hasUserBeenAlerted,
                        userPriceTarget = userPriceTarget,
                        priceTargetDirection = priceTargetDirection,
                        completedOnDate = completedOnDate
                    )
                )
            }
        }
        return domainList
    }

    override fun reverseTransformation(domainList: List<PriceTargetDomain>): List<PriceTargetEntity> {
        val entityList = arrayListOf<PriceTargetEntity>()
        domainList.map {
            it.apply {
                entityList.add(
                    PriceTargetEntity(
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
                        hasPriceTargetBeenHit = hasPriceTargetBeenHit,
                        hasUserBeenAlerted = hasUserBeenAlerted,
                        userPriceTarget = userPriceTarget,
                        priceTargetDirection = priceTargetDirection,
                        completedOnDate = completedOnDate
                    )
                )
            }
        }
        return entityList
    }
}