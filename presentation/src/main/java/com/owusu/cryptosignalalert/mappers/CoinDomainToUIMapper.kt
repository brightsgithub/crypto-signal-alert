package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.models.CoinUI

class CoinDomainToUIMapper: UIListMapper<CoinDomain, CoinUI> {
    override fun mapDomainListToUIList(domainList: List<CoinDomain>): List<CoinUI> {
        val list = arrayListOf<CoinUI>()
        domainList.forEach {
            it.apply {
                list.add(
                    CoinUI(
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
                        marketCapRank = marketCapRank,
                        maxSupply = maxSupply,
                        name = name,
                        priceChange24h = priceChange24h,
                        priceChangePercentage24h = priceChangePercentage24h,
                        symbol = symbol,
                        totalSupply = totalSupply,
                        totalVolume = totalVolume
                    )
                )
            }
        }
        return list
    }

    override fun mapUIListToDomainList(uiList: List<CoinUI>): List<CoinDomain> {
        val list = arrayListOf<CoinDomain>()
        uiList.forEach {
            it.apply {
                list.add(
                    CoinDomain(
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
                        marketCapRank = marketCapRank,
                        maxSupply = maxSupply,
                        name = name,
                        priceChange24h = priceChange24h,
                        priceChangePercentage24h = priceChangePercentage24h,
                        symbol = symbol,
                        totalSupply = totalSupply,
                        totalVolume = totalVolume
                    )
                )
            }
        }
        return list
    }
}