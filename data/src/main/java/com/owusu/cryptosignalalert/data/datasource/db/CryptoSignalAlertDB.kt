package com.owusu.cryptosignalalert.data.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity


/**
 * Created by Bright Owusu-Amankwaa on 2019-09-05.
 */
@Database(entities = [
    PriceTargetEntity::class
],
    version = 1)
abstract class CryptoSignalAlertDB : RoomDatabase() {

    abstract fun priceTargetDao(): PriceTargetDao

    companion object {
        @Volatile private var instance: CryptoSignalAlertDB? = null
        private val LOCK = Any()

        operator fun invoke(appContext: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(appContext).also { instance = it }
        }


        private fun buildDatabase(appContext: Context) : CryptoSignalAlertDB {
            return Room.databaseBuilder(appContext.applicationContext,
                CryptoSignalAlertDB::class.java, "csa_database.db")
                .build()
        }
    }

}