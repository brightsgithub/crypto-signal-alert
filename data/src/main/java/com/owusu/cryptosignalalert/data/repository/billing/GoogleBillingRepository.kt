package com.owusu.cryptosignalalert.data.repository.billing

import android.util.Log
import com.android.billingclient.api.SkuDetails
import com.owusu.cryptosignalalert.domain.repository.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GoogleBillingRepository(
    private val billingDataSource: BillingDataSource,
    private val defaultScope: CoroutineScope
): BillingRepository {

    val billingFlowInProcess: Flow<Boolean>
        get() = billingDataSource.getBillingFlowInProcess()

    init {
        postMessagesFromBillingFlow()

        // Since both are tied to application lifecycle, we can launch this scope to collect
        // consumed purchases from the billing data source while the app process is alive.
        defaultScope.launch {
            billingDataSource.getConsumedPurchases().collect { }
        }
    }

    fun getSkuDescription(sku: String): Flow<SkuDetails> {
        return billingDataSource.getSkuDetails(sku)
    }

    /**
     * Sets up the event that we can use to send messages up to the UI to be used in Snackbars.
     * This collects new purchase events from the BillingDataSource, transforming the known SKU
     * strings into useful String messages, and emitting the messages into the game messages flow.
     */
    private fun postMessagesFromBillingFlow() {
        defaultScope.launch {
            try {
                billingDataSource.getNewPurchases().collect { skuList ->
                    // TODO: Handle multi-line purchases better
                    for ( sku in skuList ) {
                        when (sku) {
//                            SKU_UNLOCK_ALL -> newPurchase.emit(R.string.message_more_gas_acquired)
//                            SKU_UNLIMITED_ALERTS -> gameMessages.emit(R.string.message_premium)
//                            SKU_REMOVE_ADS -> gameMessages.emit(R.string.message_premium)
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.d(TAG, "Collection complete")
            }
            Log.d(TAG, "Collection Coroutine Scope Exited")
        }
    }

    companion object {
        val TAG = "BillingRepository"

        private val SKU_UNLOCK_ALL = "com.owusu.cryptosignalalert.unlock_all"
        private val SKU_UNLIMITED_ALERTS = "com.owusu.cryptosignalalert.unlimted_alerts"
        private val SKU_REMOVE_ADS = "com.owusu.cryptosignalalert.remove_ads"
        val INAPP_SKUS = arrayOf(SKU_UNLOCK_ALL, SKU_UNLIMITED_ALERTS, SKU_REMOVE_ADS)
    }
}