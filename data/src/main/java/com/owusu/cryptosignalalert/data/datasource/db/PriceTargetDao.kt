package com.owusu.cryptosignalalert.data.datasource.db

import androidx.room.*
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity

@Dao
interface PriceTargetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceTargets(priceTargets: List<PriceTargetEntity>)

    @Update
    suspend fun updatePriceTargets(priceTargets: List<PriceTargetEntity>)

    @Delete
    suspend fun deletePriceTargets(priceTargets: List<PriceTargetEntity>) : Int

    @Query("SELECT * FROM price_targets_table pt where pt.has_price_target_been_hit = 1")
    suspend fun getPriceTargets() : List<PriceTargetEntity>

    @Query("SELECT * FROM price_targets_table pt")
    suspend fun getPriceTargetsToAlertUser() : List<PriceTargetEntity>
}