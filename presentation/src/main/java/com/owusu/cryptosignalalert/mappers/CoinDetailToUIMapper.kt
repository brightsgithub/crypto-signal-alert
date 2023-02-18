package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.CoinDetailDomain
import com.owusu.cryptosignalalert.domain.models.ImageDomain
import com.owusu.cryptosignalalert.models.CoinDetailUI
import com.owusu.cryptosignalalert.models.ImageUI

class CoinDetailToUIMapper {
    fun map(coinDetail: CoinDetailDomain): CoinDetailUI {
        return CoinDetailUI(
            blockTimeInMinutes = coinDetail.blockTimeInMinutes,
            coingeckoRank = coinDetail.coingeckoRank,
            description = coinDetail.description,
            id = coinDetail.id,
            image = coinDetail.image?.let { mapImageDomainToUI(it) },
            lastUpdated = coinDetail.lastUpdated,
            marketCapRank = coinDetail.marketCapRank,
            name = coinDetail.name,
            symbol = coinDetail.symbol
        )
    }

    private fun mapImageDomainToUI(imageDomain: ImageDomain): ImageUI {
        return ImageUI(
            large = imageDomain.large,
            small = imageDomain.small,
            thumb = imageDomain.thumb
        )
    }
}

