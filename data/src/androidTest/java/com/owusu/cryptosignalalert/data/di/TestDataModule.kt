package com.owusu.cryptosignalalert.data.di

import android.content.Context
import androidx.room.Room
import com.owusu.cryptosignalalert.data.datasource.db.CryptoSignalAlertDB
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Here we can override dependencies
 */
class TestDataModule(private val context: Context): DataModuleWrapper(context) {

    val testDataModule = super.dataModule

    init {
        testDataModule.single<CryptoSignalAlertDB> {
            // https://developer.android.com/training/data-storage/room/testing-db
            Room.inMemoryDatabaseBuilder(
                context, CryptoSignalAlertDB::class.java
            ).build()
        }
    }
//
//    override fun getDataModule(): Module {
//
//        // Get the original dataModule and override what we need.
//        dataModule.single<CryptoSignalAlertDB> {
//            // https://developer.android.com/training/data-storage/room/testing-db
//            Room.inMemoryDatabaseBuilder(
//                context, CryptoSignalAlertDB::class.java
//            ).build()
//        }
//
//        return dataModule
//    }
}