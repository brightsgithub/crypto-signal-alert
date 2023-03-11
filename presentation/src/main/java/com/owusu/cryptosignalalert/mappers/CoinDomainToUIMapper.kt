package com.owusu.cryptosignalalert.mappers

import androidx.compose.runtime.mutableStateOf
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.util.PriceDisplayUtils
import com.owusu.cryptosignalalert.util.PriceUtils
import java.math.BigDecimal

class CoinDomainToUIMapper(private val priceDisplayUtils: PriceDisplayUtils) {

    fun mapDomainToUI(
        coinDomain: CoinDomain,
        priceTargetsMap: Map<String,PriceTargetDomain> = emptyMap()
    ): CoinUI {

        val priceTargetDomain = priceTargetsMap[coinDomain.id]

        coinDomain.apply {
            return CoinUI(
                ath = ath,
                athChangePercentage = athChangePercentage,
                athDate = athDate,
                atl = atl,
                atlChangePercentage = atlChangePercentage,
                atlDate = atlDate,
                circulatingSupply = circulatingSupply,
                currentPrice = currentPrice,
                currentPriceStr = priceDisplayUtils.convertPriceToString(currentPrice),
                fullyDilutedValuation = fullyDilutedValuation,
                high24h = high24h,
                id = id,
                image = image,
                lastUpdated = lastUpdated,
                low24h = low24h,
                marketCap = marketCap,
                marketCapStr = priceDisplayUtils.convertPriceToString(marketCap),
                marketCapChange24h = marketCapChange24h,
                marketCapChangePercentage24h = marketCapChangePercentage24h,
                marketCapRank = marketCapRank?.toInt(),
                maxSupply = maxSupply,
                name = name,
                priceChange24h = priceChange24h,
                priceChangePercentage24h = priceDisplayUtils.convertToDecimalPlace(priceChangePercentage24h, 2),
                priceChangePercentage24hStr = applySymbols(priceDisplayUtils.convertToDecimalPlace(priceChangePercentage24h, 2)),
                is24HrPriceChangePositive = isPricePositive(priceChange24h),
                symbol = symbol,
                totalSupply = totalSupply,
                totalVolume = totalVolume,
                hasPriceTarget = mutableStateOf(priceTargetDomain != null),
                userPriceTarget = priceTargetDomain?.userPriceTarget,
                userPriceTargetDisplay = priceDisplayUtils.convertPriceToString(priceTargetDomain?.userPriceTarget)
            )
        }
    }

    private fun applySymbols(number: Double?): String? {

        if (number == null) {
            return ""
        }

        return if (number > 0) {
            "+"+number+"%"
        } else {
            return number.toString()+"%"
        }
    }

    private fun isPricePositive(number: Double?): Boolean {
        if (number == null) {
            return false
        }
        return number > 0
    }
}