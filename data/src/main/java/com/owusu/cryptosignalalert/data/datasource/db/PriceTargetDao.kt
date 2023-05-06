package com.owusu.cryptosignalalert.data.datasource.db

import androidx.room.*
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceTargetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceTargets(priceTargets: List<PriceTargetEntity>): List<Long>

    @Update
    suspend fun updatePriceTargets(priceTargets: List<PriceTargetEntity>)

    @Delete
    suspend fun deletePriceTargets(priceTargets: List<PriceTargetEntity>) : Int

    @Query("SELECT * FROM price_targets_table pt")
    fun getPriceTargets() : Flow<List<PriceTargetEntity>>

    @Query("SELECT COUNT(*) FROM price_targets_table")
    suspend fun getPriceTargetsCount() : Int
    @Query("SELECT COUNT(*) FROM price_targets_table pt where pt.has_price_target_been_hit = 0 and pt.has_user_been_alerted = 0")
    suspend fun getPriceTargetsThatHaveNotBeenHitCount() : Int

    @Query("SELECT * FROM price_targets_table pt where pt.has_price_target_been_hit = 1 and pt.has_user_been_alerted = 0")
    suspend fun getPriceTargetsToAlertUser() : List<PriceTargetEntity>

    @Query("SELECT * FROM price_targets_table pt where pt.has_price_target_been_hit = 0 and pt.has_user_been_alerted = 0")
    fun getPriceTargetsThatHaveNotBeenHit() : Flow<List<PriceTargetEntity>>

    @Query("DELETE FROM price_targets_table")
    suspend fun nukeTable()
}