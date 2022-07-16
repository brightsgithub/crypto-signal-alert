package com.owusu.cryptosignalalert.data.mappers

import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper
import com.owusu.cryptosignalalert.domain.models.PriceWrapper

class PriceAPIMapper: DataAPIMapper<PriceAPIWrapper, PriceWrapper> {
    override fun mapAPIToDomain(api: PriceAPIWrapper): PriceWrapper {
        TODO("Not yet implemented")
    }

    override fun mapToDomainApi(domain: PriceWrapper): PriceAPIWrapper {
        TODO("Not yet implemented")
    }

}