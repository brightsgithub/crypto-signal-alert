package com.owusu.cryptosignalalert.mappers

interface UIListMapper<Domain, UI> {
    fun mapDomainListToUIList(domainList: List<Domain>): List<UI>
    fun mapUIListToDomainList(uiList: List<UI>): List<Domain>
}