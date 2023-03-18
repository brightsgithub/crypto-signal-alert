package com.owusu.cryptosignalalert.domain.repository

interface AppPreferencesRepository {

    fun isPriceTargetLimitPurchased(): Boolean
    fun makePriceTargetLimitFree()
    fun makePriceTargetLimitPurchasable()

    fun isAdsPurchased(): Boolean
    fun makeAdsFree()
    fun makeAdsPurchasable()

    fun isAppFree(): Boolean
    fun makeAppFree()
    fun makeAppPurchasable()

    fun hasCoinIdsBeenPopulated(): Boolean
    fun coinIdsHaveBeenPopulated()
    fun coinIdsHaveNotBeenPopulated()
}