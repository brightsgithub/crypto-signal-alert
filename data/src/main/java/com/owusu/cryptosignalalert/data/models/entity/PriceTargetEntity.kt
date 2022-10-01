package com.owusu.cryptosignalalert.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection

@Entity(tableName = "price_targets_table")
data class PriceTargetEntity(

    /**
     * https://stackoverflow.com/questions/44109700/how-to-make-primary-key-as-autoincrement-for-room-persistence-lib
     * If the field type is long or int (or its TypeConverter converts it to a long or int),
     * Insert methods treat 0 as not-set while inserting the item.
     *
     *  If the field's type is Integer or Long (Object) (or its TypeConverter converts it to an
     *  Integer or a Long), Insert methods treat null as not-set while inserting the item.
     */
    @PrimaryKey(autoGenerate = true)
    var localPrimeId: Int,
    @ColumnInfo(name="id")
    val id: String,
    @ColumnInfo(name="ath")
    val ath: Double? = null,
    @ColumnInfo(name="ath_change_percentage")
    val athChangePercentage: Double? = null,
    @ColumnInfo(name="ath_date")
    val athDate: String? = null,
    @ColumnInfo(name="atl")
    val atl: Double? = null,
    @ColumnInfo(name="atl_change_percentage")
    val atlChangePercentage: Double? = null,
    @ColumnInfo(name="atl_date")
    val atlDate: String? = null,
    @ColumnInfo(name="circulating_supply")
    val circulatingSupply: Double? = null,
    @ColumnInfo(name="current_price")
    val currentPrice: Double? = null,
    @ColumnInfo(name="fully_diluted_valuation")
    val fullyDilutedValuation: Double? = null,
    @ColumnInfo(name="high_24h")
    val high24h: Double? = null,
    @ColumnInfo(name="image")
    val image: String? = null,
    @ColumnInfo(name="last_updated")
    val lastUpdated: String? = null,
    @ColumnInfo(name="low_24h")
    val low24h: Double? = null,
    @ColumnInfo(name="market_cap")
    val marketCap: Double? = null,
    @ColumnInfo(name="market_cap_change_24h")
    val marketCapChange24h: Double? = null,
    @ColumnInfo(name="market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double? = null,
    @ColumnInfo(name="market_cap_rank")
    val marketCapRank: Double? = null,
    @ColumnInfo(name="max_supply")
    val maxSupply: Double? = null,
    @ColumnInfo(name="name")
    val name: String? = null,
    @ColumnInfo(name="price_change_24h")
    val priceChange24h: Double? = null,
    @ColumnInfo(name="price_change_percentage_24h")
    val priceChangePercentage24h: Double? = null,
    @ColumnInfo(name="symbol")
    val symbol: String? = null,
    @ColumnInfo(name="total_supply")
    val totalSupply: Double? = null,
    @ColumnInfo(name="total_volume")
    val totalVolume: Double? = null,
    @ColumnInfo(name="user_price_target")
    var userPriceTarget: Double? = null,
    @ColumnInfo(name = "has_price_target_been_hit")
    var hasPriceTargetBeenHit: Boolean = false,
    @ColumnInfo(name = "has_user_been_alerted")
    var hasUserBeenAlerted: Boolean = false,
    @ColumnInfo(name = "price_target_direction")
    var priceTargetDirection: PriceTargetDirection
)