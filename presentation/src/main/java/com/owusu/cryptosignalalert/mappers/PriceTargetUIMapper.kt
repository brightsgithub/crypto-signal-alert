package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.util.PriceDisplayUtils
import java.math.BigDecimal

class PriceTargetUIMapper(private val priceDisplayUtils: PriceDisplayUtils):UIMapper<PriceTargetDomain, PriceTargetUI> {
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
                        currentPriceDisplay = priceDisplayUtils.convertPriceToString(currentPrice),
                        fullyDilutedValuation = fullyDilutedValuation,
                        high24h = high24h,
                        image = image,
                        lastUpdated = lastUpdated,
                        low24h = low24h,
                        marketCap = marketCap,
                        marketCapChange24h = marketCapChange24h,
                        marketCapChangePercentage24h = marketCapChangePercentage24h,
                        marketCapRank = marketCapRank?.toInt(),
                        maxSupply = maxSupply,
                        name = name,
                        priceChange24h = priceChange24h,
                        priceChangePercentage24h = priceChangePercentage24h,
                        symbol = symbol,
                        totalSupply = totalSupply,
                        totalVolume = totalVolume,
                        userPriceTarget = userPriceTarget,
                        userPriceTargetDisplay = priceDisplayUtils.convertPriceToString(userPriceTarget),
                        hasPriceTargetBeenHit = hasPriceTargetBeenHit,
                        hasUserBeenAlerted = hasUserBeenAlerted,
                        priceTargetDirection = mapPriceDirectionToUI(priceTargetDirection),
                        progress = calculateProgress(this),
                        progressPercentageDisplay = getProgressPercentageDisplay(this),
                        completedOnDate = completedOnDate
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
                        marketCapRank = marketCapRank?.toDouble(),
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
                        priceTargetDirection = mapPriceDirectionToDomain(priceTargetDirection),
                        completedOnDate = completedOnDate
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

    override fun mapDomainToUI(domainObj: PriceTargetDomain): PriceTargetUI {
        TODO("Not yet implemented")
    }

    override fun mapUIToDomain(uiObj: PriceTargetUI): PriceTargetDomain {
        TODO("Not yet implemented")
    }

    private fun calculateProgress(priceTargetDomain: PriceTargetDomain) : Float {

        val currentPrice = priceTargetDomain.currentPrice
        val userPriceTarget = priceTargetDomain.userPriceTarget
        val priceTargetDir = priceTargetDomain.priceTargetDirection

        if (currentPrice == null || userPriceTarget == null) return 0f

        if (currentPrice <=0 || userPriceTarget <=0) return 0f

        if (priceTargetDomain.hasPriceTargetBeenHit && priceTargetDomain.hasUserBeenAlerted) return 1f

        return when (priceTargetDir) {
            PriceTargetDirection.ABOVE -> {
                val result = (currentPrice / userPriceTarget) //* 100
                if (result > 1) return 1f
                result.toFloat()
            }
            PriceTargetDirection.BELOW -> {
                val result = (userPriceTarget / currentPrice) //* 100
                if (result > 1) return 1f
                result.toFloat()
            }
            PriceTargetDirection.NOT_SET -> 0f
        }

    }

    private fun getProgressPercentageDisplay(priceTargetDomain: PriceTargetDomain) : String {

        val currentPrice = priceTargetDomain.currentPrice
        val userPriceTarget = priceTargetDomain.userPriceTarget
        val priceTargetDir = priceTargetDomain.priceTargetDirection

        if (currentPrice == null || userPriceTarget == null) return "0%"

        if (currentPrice <=0 || userPriceTarget <=0) return "0%"

        if (priceTargetDomain.hasPriceTargetBeenHit && priceTargetDomain.hasUserBeenAlerted) return "Completed"

        return when (priceTargetDir) {
            PriceTargetDirection.ABOVE -> {
                val result = (currentPrice / userPriceTarget) * 100
                if (result > 100) return "100%"
                priceDisplayUtils.convertToDecimalPlace(result, 2).toString() +"%"
            }
            PriceTargetDirection.BELOW -> {
                val result = (userPriceTarget / currentPrice) * 100
                if (result > 100) return "100%"
                priceDisplayUtils.convertToDecimalPlace(result, 2).toString() +"%"
            }
            PriceTargetDirection.NOT_SET -> "0%"
        }
    }
}

enum class PriceTargetDirectionUI {
    ABOVE, BELOW, NOT_SET
}