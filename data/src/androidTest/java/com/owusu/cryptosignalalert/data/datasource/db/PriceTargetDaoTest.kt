package com.owusu.cryptosignalalert.data.datasource.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.inject

@RunWith(AndroidJUnit4::class)
class PriceTargetDaoTest : KoinComponent {

    private val priceTargetDao: PriceTargetDao by inject()


    @Test
    fun testInsertPriceTargets()  = runBlocking {
        val numOfCoins = 1
        val coinsToBeAddedToBb = createPriceTargets(numOfCoins)
        priceTargetDao.insertPriceTargets(coinsToBeAddedToBb)

        val coinsListFromDb = priceTargetDao.getPriceTargets()

        Assert.assertTrue(coinsToBeAddedToBb[0].id == coinsListFromDb[0].id)
        Assert.assertTrue(coinsListFromDb.size == numOfCoins)
    }

    @Test
    fun testRemovePriceTarget()  = runBlocking {
        val numOfCoins = 1
        val coinsToBeAddedToBb = createPriceTargets(numOfCoins)

        // Insert some allowed contacts
        priceTargetDao.insertPriceTargets(coinsToBeAddedToBb)

        val coinsListFromDb = priceTargetDao.getPriceTargets()

        // Assert that the contact was inserted into the db
        Assert.assertTrue(coinsToBeAddedToBb[0].id ==  coinsListFromDb[0].id)
        Assert.assertTrue(coinsToBeAddedToBb.size == numOfCoins)

        // Now delete the contact that was added
        val numOfCoinsDeleted = priceTargetDao.deletePriceTargets(coinsListFromDb)
        Assert.assertTrue(numOfCoinsDeleted == numOfCoins)

    }

    private fun createPriceTargets(size: Int) : List<PriceTargetEntity>{

        val list = arrayListOf<PriceTargetEntity>()

        for (i in 1.. size) {
            list.add(getPriceTarget())
        }
        return list
    }

    private fun getPriceTarget(): PriceTargetEntity {
        return PriceTargetEntity(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
            currentPrice = 21518.0,
            marketCap = 411096596530.0,
            marketCapRank = 1.0,
            fullyDilutedValuation = 452245724314L,
            totalVolume = 48963778515.0,
            high24h = 22007.0,
            low24h = 21251.0,
            priceChange24h = -16.19950226412402,
            priceChangePercentage24h = -0.07523,
            marketCapChange24h = -2334324524.703125,
            marketCapChangePercentage24h = -0.56462,
            circulatingSupply = 19089243.0,
            totalSupply = 21000000.0,
            maxSupply = 21000000.0,
            ath = 69045.0,
            athChangePercentage = -68.88983,
            athDate = "2021-11-10T14:24:11.849Z",
            atl = 67.81,
            atlChangePercentage = 31577.13346,
            atlDate = "2013-07-06T00:00:00.000Z",
            lastUpdated = "2022-07-09T12:31:40.339Z",
            userPriceTarget = 22000.0,
            hasPriceTargetBeenHit = false,
            hasUserBeenAlerted = false
        )
    }
}