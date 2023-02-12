package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.api.CoinIdAPI
import com.owusu.cryptosignalalert.data.models.entity.CoinIdEntity
import com.owusu.cryptosignalalert.domain.models.CoinIdDomain

class CoinIdAPIToDomainIdMapper {
    fun apiToDomain(apiList: List<CoinIdAPI>): List<CoinIdDomain> {
        return apiList.map { api ->
            CoinIdDomain(
                id = api.id,
                name = api.name,
                symbol = api.symbol
            )
        }
    }

    fun domainToEntity(domainList: List<CoinIdDomain>): List<CoinIdEntity> {
        return domainList.map { domain ->
            CoinIdEntity(
                localPrimeId = 0,
                id = domain.id,
                name = domain.name,
                symbol = domain.symbol
            )
        }
    }

    fun entityToDomain(domainList: List<CoinIdEntity>): List<CoinIdDomain> {
        return domainList.map { domain ->
            CoinIdDomain(
                id = domain.id,
                name = domain.name,
                symbol = domain.symbol
            )
        }
    }
}
