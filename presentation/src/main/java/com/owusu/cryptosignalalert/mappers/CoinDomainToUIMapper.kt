package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.util.PriceUtils
import java.math.BigDecimal

class CoinDomainToUIMapper {

    fun mapDomainToUI(coinDomain: CoinDomain, priceTargetsMap: Map<String,PriceTargetDomain>): CoinUI {

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
                currentPriceStr = convertPriceToString(currentPrice),
                fullyDilutedValuation = fullyDilutedValuation,
                high24h = high24h,
                id = id,
                image = image,
                lastUpdated = lastUpdated,
                low24h = low24h,
                marketCap = marketCap,
                marketCapStr = convertPriceToString(marketCap),
                marketCapChange24h = marketCapChange24h,
                marketCapChangePercentage24h = marketCapChangePercentage24h,
                marketCapRank = marketCapRank?.toInt(),
                maxSupply = maxSupply,
                name = name,
                priceChange24h = priceChange24h,
                priceChangePercentage24h = convertToDecimalPlace(priceChangePercentage24h, 2),
                priceChangePercentage24hStr = applySymbols(convertToDecimalPlace(priceChangePercentage24h, 2)),
                is24HrPriceChangePositive = isPricePositive(priceChange24h),
                symbol = symbol,
                totalSupply = totalSupply,
                totalVolume = totalVolume,
                hasPriceTarget = priceTargetDomain != null,
                userPriceTarget = priceTargetDomain?.userPriceTarget,
                userPriceTargetDisplay = convertPriceToString(priceTargetDomain?.userPriceTarget)
            )
        }
    }

    private fun convertPriceToString(price: Double?): String? {
        if (price == null) return ""
        return PriceUtils.numberFormatter("USD", price.toString())
    }

    private fun convertPriceToDouble(price: String?): Double? {
        if (price == null) return 0.0
        return price.toDouble()
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

    private fun convertToDecimalPlace(price: Double?, decimalPlaces: Int): Double? {
        if (price == null) return price
        val a = BigDecimal(price)
        return a.setScale(decimalPlaces, BigDecimal.ROUND_HALF_EVEN).toDouble()
    }
}