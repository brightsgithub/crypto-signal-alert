package com.owusu.cryptosignalalert.data.mappers

interface DataAPIListMapper<Api, Domain> {
    fun transform(apiList: List<Api>): List<Domain>
    fun reverseTransformation(domainList: List<Domain>): List<Api>
}