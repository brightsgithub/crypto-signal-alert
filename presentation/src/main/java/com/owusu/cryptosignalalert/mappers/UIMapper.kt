package com.owusu.cryptosignalalert.mappers

interface UIMapper<DomainObj, UIObj> {
    fun mapDomainToUI(domainObj: DomainObj): UIObj
    fun mapUIToDomain(uiObj: UIObj): DomainObj
}