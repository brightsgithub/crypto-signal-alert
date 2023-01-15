package com.owusu.cryptosignalalert.data.mappers

import com.android.billingclient.api.SkuDetails
import com.owusu.cryptosignalalert.data.models.SkuWrapper
import com.owusu.cryptosignalalert.data.models.skus.Skus
import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SkuMapperTest {

    @MockK
    private lateinit var skuWrapper: SkuWrapper

    @MockK
    private lateinit var skuDetails: SkuDetails

    //@MockK
    private val skus = Skus

    private lateinit var skuMapper: SkuMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        skuMapper = SkuMapper()
    }

    @Test
    fun `transform should return correct SkuDetailsDomain when sku is SKU_UNLIMITED_ALERTS`() {
        every { skuWrapper.skuDetails } returns skuDetails
        every { skuWrapper.newPurchasedSku } returns null
        every { skuDetails.sku } returns Skus.SKU_UNLIMITED_ALERTS
        every { skuDetails.price } returns "$9.99"
        every { skuDetails.description } returns "Unlimited alerts"

        val skuDetailsDomain = skuMapper.transform(skuWrapper, false, skus)

        assertEquals(1, skuDetailsDomain.pos)
        assertEquals(Skus.SKU_UNLIMITED_ALERTS, skuDetailsDomain.sku)
        assertEquals("Set unlimited alerts", skuDetailsDomain.title)
        assertEquals("Unlimited alerts", skuDetailsDomain.subTitle)
        assertEquals("Unlimited alerts", skuDetailsDomain.description)
        assertEquals("$9.99", skuDetailsDomain.price)
        assertEquals(false, skuDetailsDomain.isPurchased)
        assertEquals(false, skuDetailsDomain.isBundleBuyAll)
    }

    @Test
    fun `transform should return correct SkuDetailsDomain when sku is SKU_REMOVE_ADS`() {
        every { skuWrapper.skuDetails } returns skuDetails
        every { skuWrapper.newPurchasedSku } returns null
        every { skuDetails.sku } returns Skus.SKU_REMOVE_ADS
        every { skuDetails.price } returns "$4.99"
        every { skuDetails.description } returns "Remove ads"

        val skuDetailsDomain = skuMapper.transform(skuWrapper, false, skus)

        assertEquals(2, skuDetailsDomain.pos)
        assertEquals(Skus.SKU_REMOVE_ADS, skuDetailsDomain.sku)
        assertEquals("Remove Ads", skuDetailsDomain.title)
        assertEquals("Remove those annoying ads", skuDetailsDomain.subTitle)
        assertEquals("Remove ads", skuDetailsDomain.description)
        assertEquals("$4.99", skuDetailsDomain.price)
        assertEquals(false, skuDetailsDomain.isPurchased)
        assertEquals(false, skuDetailsDomain.isBundleBuyAll)
    }

    @Test
    fun `transform should return correct SkuDetailsDomain when sku is not SKU_UNLIMITED_ALERTS or SKU_REMOVE_ADS`() {
        every { skuWrapper.skuDetails } returns skuDetails
        every { skuWrapper.newPurchasedSku } returns Skus.SKU_UNLOCK_ALL
        every { skuDetails.sku } returns "other_sku"
        every { skuDetails.price } returns "$19.99"
        every { skuDetails.description } returns "Buy all bundle"

        val skuDetailsDomain = skuMapper.transform(skuWrapper, false, skus)

        assertEquals(0, skuDetailsDomain.pos)
        assertEquals("other_sku", skuDetailsDomain.sku)
        assertEquals("Buy All Bundle", skuDetailsDomain.title)
        assertEquals("Purchase all discount", skuDetailsDomain.subTitle)
        assertEquals("Buy all bundle", skuDetailsDomain.description)
        assertEquals("$19.99", skuDetailsDomain.price)
        assertEquals(false, skuDetailsDomain.isPurchased)
        assertEquals(true, skuDetailsDomain.isBundleBuyAll)
    }

    @Test
    fun `transform should return correct SkuDetailsDomain when newPurchasedSku is not null`() {
        every { skuWrapper.skuDetails } returns skuDetails
        every { skuWrapper.newPurchasedSku } returns Skus.SKU_REMOVE_ADS
        every { skuDetails.sku } returns Skus.SKU_REMOVE_ADS
        every { skuDetails.price } returns "$4.99"
        every { skuDetails.description } returns "Remove ads"

        val skuDetailsDomain = skuMapper.transform(skuWrapper, false, skus)

        assertEquals(2, skuDetailsDomain.pos)
        assertEquals(Skus.SKU_REMOVE_ADS, skuDetailsDomain.sku)
        assertEquals("Remove Ads", skuDetailsDomain.title)
        assertEquals("Remove those annoying ads", skuDetailsDomain.subTitle)
        assertEquals("Remove ads", skuDetailsDomain.description)
        assertEquals("$4.99", skuDetailsDomain.price)
        assertEquals(true, skuDetailsDomain.isPurchased)
        assertEquals(false, skuDetailsDomain.isBundleBuyAll)
    }

    @Test
    fun `transform should return correct SkuDetailsDomain when isPurchased is true`() {
        every { skuWrapper.skuDetails } returns skuDetails
        every { skuWrapper.newPurchasedSku } returns Skus.SKU_UNLIMITED_ALERTS
        every { skuDetails.sku } returns Skus.SKU_UNLIMITED_ALERTS
        every { skuDetails.price } returns "$9.99"
        every { skuDetails.description } returns "Unlimited alerts"

        val skuDetailsDomain = skuMapper.transform(skuWrapper, true, skus)

        assertEquals(1, skuDetailsDomain.pos)
        assertEquals(Skus.SKU_UNLIMITED_ALERTS, skuDetailsDomain.sku)
        assertEquals("Set unlimited alerts", skuDetailsDomain.title)
        assertEquals("Unlimited alerts", skuDetailsDomain.subTitle)
        assertEquals("Unlimited alerts", skuDetailsDomain.description)
        assertEquals("$9.99", skuDetailsDomain.price)
        assertEquals(true, skuDetailsDomain.isPurchased)
        assertEquals(false, skuDetailsDomain.isBundleBuyAll)
    }
}
