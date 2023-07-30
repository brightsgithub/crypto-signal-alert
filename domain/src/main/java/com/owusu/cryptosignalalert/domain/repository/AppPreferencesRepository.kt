package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.states.PurchasedStateDomain
import kotlinx.coroutines.flow.Flow

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

    fun getLastCoinIdUpdate(): Long
    fun setLastCoinIdUpdate(timestamp: Long)

    fun isWorkManagerExecuting(): Boolean
    fun workManagerIsExecuting()
    fun workManagerHasFinishedExecuting()

    fun listenForPurchasedDataState(): Flow<PurchasedStateDomain>
    fun isSirenEnabled(): Boolean
    fun allowSirenSound()
    fun disableSirenSound()
}