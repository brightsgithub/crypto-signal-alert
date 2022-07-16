package com.owusu.cryptosignalalert.data.mappers

interface DataAPIMapper<Api, Domain> {
    fun mapAPIToDomain(api: Api): Domain
    fun mapToDomainApi(domain: Domain): Api
}