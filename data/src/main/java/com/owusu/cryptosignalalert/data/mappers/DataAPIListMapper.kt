package com.owusu.cryptosignalalert.data.mappers

interface DataAPIListMapper<Api, Domain> {
    fun mapAPIToDomain(apiList: List<Api>): List<Domain>
    fun mapToDomainApi(domainList: List<Domain>): List<Api>
}