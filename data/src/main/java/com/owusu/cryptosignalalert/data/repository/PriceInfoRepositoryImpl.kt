package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.PricesDataSource
import com.owusu.cryptosignalalert.data.mappers.DataMapper
import com.owusu.cryptosignalalert.data.models.api.PriceAPIWrapper
import com.owusu.cryptosignalalert.domain.models.PriceWrapperDomain
import com.owusu.cryptosignalalert.domain.repository.PriceInfoRepository

class PriceInfoRepositoryImpl(
    private val pricesDataSource: PricesDataSource,
    private val priceMapper: DataMapper<PriceAPIWrapper, PriceWrapperDomain>
): PriceInfoRepository {

    override suspend fun getPrices(ids: String, currencies: String): PriceWrapperDomain {
        val priceAPIWrapper = pricesDataSource.getPrices(ids, currencies)
        val wrapper = priceMapper.transform(priceAPIWrapper)
        return wrapper
    }
}