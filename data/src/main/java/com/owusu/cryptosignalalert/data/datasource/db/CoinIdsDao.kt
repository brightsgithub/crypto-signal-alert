package com.owusu.cryptosignalalert.data.datasource.db

import androidx.room.*
import com.owusu.cryptosignalalert.data.models.entity.CoinIdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinIdsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoinIds(coinIds: List<CoinIdEntity>)

    @Query("SELECT * FROM coin_id_table WHERE LOWER(id) LIKE '%' || LOWER(:searchStr) || '%' OR LOWER(name) LIKE '%' || LOWER(:searchStr) || '%' OR LOWER(symbol) LIKE '%' || LOWER(:searchStr) || '%'")
    fun searchCoinIds(searchStr: String): Flow<List<CoinIdEntity>>

    @Query("SELECT * FROM coin_id_table")
    suspend fun getAllLocalCoinIds(): List<CoinIdEntity>

    @Query("DELETE FROM coin_id_table")
    suspend fun nukeTable()

}