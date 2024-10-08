package com.owusu.cryptosignalalert.data.repository.billing

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.SkuDetails
import com.owusu.cryptosignalalert.data.mappers.DataMapper
import com.owusu.cryptosignalalert.data.mappers.SkuMapper
import com.owusu.cryptosignalalert.data.models.SkuWrapper
import com.owusu.cryptosignalalert.data.models.skus.Skus
import com.owusu.cryptosignalalert.data.models.skus.Skus.INAPP_SKUS
import com.owusu.cryptosignalalert.data.models.skus.Skus.INAPP_SKUS_MAP
import com.owusu.cryptosignalalert.data.models.skus.Skus.SKU_REMOVE_ADS
import com.owusu.cryptosignalalert.data.models.skus.Skus.SKU_UNLIMITED_ALERTS
import com.owusu.cryptosignalalert.data.models.skus.Skus.SKU_UNLOCK_ALL
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import com.owusu.cryptosignalalert.domain.models.states.BillingReadyState
import com.owusu.cryptosignalalert.domain.models.states.NewPurchasesState
import com.owusu.cryptosignalalert.domain.repository.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GoogleBillingRepository(
    private val billingDataSource: BillingDataSource,
    private val defaultScope: CoroutineScope,
    private val skuMapper: SkuMapper
): BillingRepository {

    private val _newPurchasesFlow = MutableStateFlow<String?>(null)
    private val newPurchasesFlow: Flow<String?> = _newPurchasesFlow

    override val billingFlowInProcess: Flow<Boolean>
        get() = billingDataSource.getBillingFlowInProcess()

    init {
        postMessagesFromBillingFlow()

        // Since both are tied to application lifecycle, we can launch this scope to collect
        // consumed purchases from the billing data source while the app process is alive.
        defaultScope.launch {
            billingDataSource.getConsumedPurchases().collect {
                Log.d(TAG, "getConsumedPurchases() -> " +it)
            }
        }
    }

    private fun observeNewPurchases(): Flow<String?> {
        return newPurchasesFlow
    }

    /**
     * Automatic support for upgrading/downgrading subscription.
     * @param activity
     * @param sku
     */
    override fun buySku(screenProxy: ScreenProxy, sku: String) {
        screenProxy as Activity
        billingDataSource.launchBillingFlow(screenProxy, sku)
    }

    override fun observeBillingReadyStateFlow(): Flow<BillingReadyState> {
        return billingDataSource.observeBillingReadyStateFlow()
    }

    override suspend fun refresh() {
        billingDataSource.refresh()
    }

    override fun getSkuIds(): List<String> {
        return INAPP_SKUS.toList()
    }

    override fun getSkuIdsMap(): Map<String, String> {
        return INAPP_SKUS_MAP
    }

    override suspend fun getSkuDetails(skus: List<String>?): List<SkuDetailsDomain> {
        val skuList = skus ?: getSkuIds()
        val skDetailsDomainList = mutableListOf<SkuDetailsDomain>()

        for(sku: String in skuList) {
            skDetailsDomainList.add(
                billingDataSource.getSkuDetails(sku)
                    .map {
                            skuDet -> SkuWrapper(skuDet!!)
                    }
                    .combine(observeNewPurchases()) { skuDetailsWrapper, newPurchasedSku ->
                        skuDetailsWrapper.copy(newPurchasedSku = newPurchasedSku)
                    }
                    .zip(isPurchased(sku)) { skuDetails, isPurchased->
                        skuMapper.transform(skuDetails, isPurchased, Skus)
                    }.first())
        }

        skDetailsDomainList.sortBy { it.pos }
        return skDetailsDomainList
    }

    private fun testChangePurchasedState() {
        defaultScope.launch {
            delay(10000)
            Log.v(TAG, "testChangePurchasedState called")
            billingDataSource.publishReloadSkuData()
        }
    }

    /**
     * We can buy gas if:
     * The billing data source allows us to purchase, which means that the item isn't already purchased.
     *
     * @param sku the SKU to get and observe the value for
     * @return Flow<Boolean> that returns true if the sku can be purchased
     */
    fun canPurchase(sku: String): Flow<Boolean> {
        return billingDataSource.canPurchase(sku)
    }

    /**
     * Return Flow that indicates whether the sku is currently purchased.
     *
     * @param sku the SKU to get and observe the value for
     * @return Flow that returns true if the sku is purchased.
     */
    fun isPurchased(sku: String): Flow<Boolean> {
        return billingDataSource.isPurchased(sku)
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
                    Log.d(TAG, "postMessagesFromBillingFlow()!!!!!")
                    billingDataSource.publishReloadSkuData()
                }
            } catch (e: Throwable) {
                Log.d(TAG, "Collection complete")
            }
            Log.d(TAG, "Collection Coroutine Scope Exited")
        }
    }

    companion object {
        val TAG = "BillingRepository"
    }
}