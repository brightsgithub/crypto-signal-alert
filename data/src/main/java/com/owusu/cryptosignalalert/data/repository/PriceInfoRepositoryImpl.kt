package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.PricesDataSource
import com.owusu.cryptosignalalert.data.mappers.DataAPIMapper
import com.owusu.cryptosignalalert.data.models.PriceAPIWrapper
import com.owusu.cryptosignalalert.domain.models.PriceWrapper
import com.owusu.cryptosignalalert.domain.repository.PriceInfoRepository

class PriceInfoRepositoryImpl(
    private val pricesDataSource: PricesDataSource,
    private val priceMapper: DataAPIMapper<PriceAPIWrapper, PriceWrapper>
): PriceInfoRepository {

    override suspend fun getPrices(ids: String, currencies: String): PriceWrapper {
        val priceAPIWrapper = pricesDataSource.getPrices(ids, currencies)
        val wrapper = priceMapper.mapAPIToDomain(priceAPIWrapper)
        return wrapper
    }
}