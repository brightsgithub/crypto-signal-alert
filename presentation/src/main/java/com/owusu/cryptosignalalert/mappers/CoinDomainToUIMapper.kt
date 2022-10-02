package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.util.PriceUtils

class CoinDomainToUIMapper: UIMapper<CoinDomain, CoinUI> {
    override fun mapDomainListToUIList(domainList: List<CoinDomain>): List<CoinUI> {
        val list = arrayListOf<CoinUI>()
        domainList.forEach {
            list.add(
                mapDomainToUI(it)
            )
        }
        return list
    }

    override fun mapUIListToDomainList(uiList: List<CoinUI>): List<CoinDomain> {
        val list = arrayListOf<CoinDomain>()
        uiList.forEach {
            list.add(
                mapUIToDomain(it)
            )
        }
        return list
    }

    override fun mapDomainToUI(domainObj: CoinDomain): CoinUI {
        domainObj.apply {
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
                priceChangePercentage24h = priceChangePercentage24h,
                priceChangePercentage24hStr = applySymbols(priceChangePercentage24h),
                is24HrPriceChangePositive = isPricePositive(priceChange24h),
                symbol = symbol,
                totalSupply = totalSupply,
                totalVolume = totalVolume
            )
        }
    }

    override fun mapUIToDomain(uiObj: CoinUI): CoinDomain {
        uiObj.apply {
            return CoinDomain(
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
                lastUpdated = lastUpdated,
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
                totalVolume = totalVolume
            )
        }
    }

    private fun convertPriceToString(price: Double?): String? {
        if (price == null) return "TBA"
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
            "+"+number
        } else {
            return number.toString()
        }
    }

    private fun isPricePositive(number: Double?): Boolean {
        if (number == null) {
            return false
        }
        return number > 0
    }
}