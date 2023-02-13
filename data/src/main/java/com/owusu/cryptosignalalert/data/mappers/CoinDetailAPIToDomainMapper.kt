package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.api.coindetail.CoinDetailAPI
import com.owusu.cryptosignalalert.domain.models.CoinDetailDomain
import com.owusu.cryptosignalalert.domain.models.ImageDomain

class CoinDetailAPIToDomainMapper {
    fun mapToDomain(apiCoinDetail: CoinDetailAPI): CoinDetailDomain {
        return CoinDetailDomain(
            blockTimeInMinutes = apiCoinDetail.blockTimeInMinutes,
            coingeckoRank = apiCoinDetail.coingeckoRank,
            description = apiCoinDetail.description?.en,
            id = apiCoinDetail.id,
            image = apiCoinDetail.image?.let { ImageDomain(it.large, it.small, it.thumb) },
            lastUpdated = apiCoinDetail.lastUpdated,
            marketCapRank = apiCoinDetail.marketCapRank,
            name = apiCoinDetail.name,
            symbol = apiCoinDetail.symbol
        )
    }
}

