package com.owusu.cryptosignalalert.data.datasource.db

import androidx.room.*
import com.owusu.cryptosignalalert.data.models.entity.CoinIdEntity

@Dao
interface CoinIdsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinIds(coinIds: List<CoinIdEntity>)

    @Query("SELECT * FROM coin_id_table WHERE id LIKE '%' || :searchStr || '%' OR name LIKE '%' || :searchStr || '%' OR symbol LIKE '%' || :searchStr || '%'")
    suspend fun searchCoinIds(searchStr: String): List<CoinIdEntity>

    @Query("SELECT * FROM coin_id_table")
    suspend fun getAllLocalCoinIds(): List<CoinIdEntity>

    @Query("DELETE FROM coin_id_table")
    suspend fun nukeTable()

}