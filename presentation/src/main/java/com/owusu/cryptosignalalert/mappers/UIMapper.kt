package com.owusu.cryptosignalalert.mappers

interface UIMapper<Domain, UI> {
    fun mapDomainListToUIList(domainList: List<Domain>): List<UI>
    fun mapUIListToDomainList(uiList: List<UI>): List<Domain>
    fun mapDomainToUI(domainObj: Domain): UI
    fun mapUIToDomain(uiObj: UI): Domain
}