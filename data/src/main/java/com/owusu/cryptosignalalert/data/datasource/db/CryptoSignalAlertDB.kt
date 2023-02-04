package com.owusu.cryptosignalalert.data.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity

/**
 * Created by Bright Owusu-Amankwaa on 2019-09-05.
 */
@Database(entities = [PriceTargetEntity::class], version = 2)
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
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        @Suppress("UNUSED")
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE price_targets_table ADD COLUMN completed_on_date TEXT")
                database.execSQL("UPDATE price_targets_table SET completed_on_date = last_updated WHERE has_price_target_been_hit = 1")

            }
        }
    }
}