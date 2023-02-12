package com.owusu.cryptosignalalert.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_id_table")
data class CoinIdEntity(
    @PrimaryKey(autoGenerate = true)
    var localPrimeId: Int,
    @ColumnInfo(name="id")
    val id: String,
    @ColumnInfo(name="name")
    val name: String,
    @ColumnInfo(name="symbol")
    val symbol: String
)
