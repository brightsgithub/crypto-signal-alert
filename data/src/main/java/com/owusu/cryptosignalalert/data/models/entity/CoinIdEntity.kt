package com.owusu.cryptosignalalert.data.models.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_id_table")
data class CoinIdEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localPrimeId")
    val localPrimeId: Int = 0,
    @NonNull
    @ColumnInfo(name = "id")
    val id: String,
    @NonNull
    @ColumnInfo(name = "name")
    val name: String,
    @NonNull
    @ColumnInfo(name = "symbol")
    val symbol: String
)
