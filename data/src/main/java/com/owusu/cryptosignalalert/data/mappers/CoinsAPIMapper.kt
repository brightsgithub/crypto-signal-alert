package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.domain.models.CoinDomain

class CoinsAPIMapper: DataAPIListMapper<CoinAPI, CoinDomain> {

    override fun mapAPIToDomain(apiList: List<CoinAPI>): List<CoinDomain> {
        val coins = arrayListOf<CoinDomain>()
        for (coinApi in apiList) {
            coinApi.apply {
                coins.add(
                    CoinDomain(
                        ath,
                        athChangePercentage,
                        athDate,
                        atl,
                        atlChangePercentage,
                        atlDate,
                        circulatingSupply,
                        currentPrice,
                        fullyDilutedValuation,
                        high24h,
                        id,
                        image,
                        lastUpdated,
                        low24h,
                        marketCap,
                        marketCapChange24h,
                        marketCapChangePercentage24h,
                        marketCapRank,
                        maxSupply,
                        name,
                        priceChange24h,
                        priceChangePercentage24h,
                        symbol,
                        totalSupply,
                        totalVolume
                    )
                )
            }
        }
        return coins
    }

    override fun mapToDomainApi(domainList: List<CoinDomain>): List<CoinAPI> {

        val coinsApi = arrayListOf<CoinAPI>()
        for (coin in domainList) {
            coin.apply {
                coinsApi.add(
                    CoinAPI(
                        ath,
                        athChangePercentage,
                        athDate,
                        atl,
                        atlChangePercentage,
                        atlDate,
                        circulatingSupply,
                        currentPrice,
                        fullyDilutedValuation,
                        high24h,
                        id,
                        image,
                        lastUpdated,
                        low24h,
                        marketCap,
                        marketCapChange24h,
                        marketCapChangePercentage24h,
                        marketCapRank,
                        maxSupply,
                        name,
                        priceChange24h,
                        priceChangePercentage24h,
                        symbol,
                        totalSupply,
                        totalVolume
                    )
                )
            }
        }
        return coinsApi
    }
}