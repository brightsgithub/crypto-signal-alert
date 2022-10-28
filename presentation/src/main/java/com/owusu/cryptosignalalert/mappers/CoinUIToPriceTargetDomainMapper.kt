package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.models.CoinUI
import java.util.*

class CoinUIToPriceTargetDomainMapper(private val dateUtils: CryptoDateUtils) {

    fun mapUIToDomain(coinUI: CoinUI, newUserPriceTarget: String): PriceTargetDomain {
        coinUI.apply {
            return PriceTargetDomain(
                localPrimeId = 0,
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
                id = id,
                image = image,
                lastUpdated = dateUtils.convertDateToFormattedStringWithTime(Calendar.getInstance().timeInMillis),
                low24h = low24h,
                marketCap = marketCap,
                marketCapChange24h = marketCapChange24h,
                marketCapChangePercentage24h = marketCapChangePercentage24h,
                marketCapRank = marketCapRank?.toDouble(),
                maxSupply = maxSupply,
                name = name,
                priceChange24h = priceChange24h,
                priceChangePercentage24h = priceChangePercentage24h,
                symbol = symbol,
                totalSupply = totalSupply,
                totalVolume = totalVolume,
                userPriceTarget = newUserPriceTarget.toDouble(),
                hasPriceTargetBeenHit = false,
                hasUserBeenAlerted = false,
                priceTargetDirection = getPriceDirection(currentPrice, newUserPriceTarget.toDouble())
            )
        }
    }

    private fun getPriceDirection(
        currentPrice: Double?,
        userPriceTarget: Double?): PriceTargetDirection {

        if (currentPrice == null || userPriceTarget == null){
            return PriceTargetDirection.NOT_SET
        }

        return if (currentPrice > userPriceTarget) {
            PriceTargetDirection.BELOW
        } else {
            PriceTargetDirection.ABOVE
        }
    }
}