package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

class PriceTargetEntityToPriceTargetDomainMapper: DataMapper<PriceTargetEntity, PriceTargetDomain> {
    override fun transform(priceTargetEntity: PriceTargetEntity): PriceTargetDomain {
        priceTargetEntity.apply {
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
                totalVolume = totalVolume
            )
        }
    }

    override fun reverseTransformation(priceTargetDomain: PriceTargetDomain): PriceTargetEntity {
        priceTargetDomain.apply {
            return PriceTargetEntity(
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
                totalVolume = totalVolume
            )
        }
    }
}