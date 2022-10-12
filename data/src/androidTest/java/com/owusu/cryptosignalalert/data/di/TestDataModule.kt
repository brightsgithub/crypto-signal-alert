package com.owusu.cryptosignalalert.data.di

import android.content.Context
import androidx.room.Room
import com.owusu.cryptosignalalert.data.datasource.db.CryptoSignalAlertDB
import org.koin.dsl.module

/**
 * Here we can override dependencies
 */
class TestDataModule(private val context: Context): DataModuleWrapper(context) {


    /**
     * Overriding definition or module (3.1.0+)
    New Koin override strategy allow to override any definition by default.
    You don't need to specify override = true anymore in your module.
    If you have 2 definitions in different modules, that have the same mapping,
    the last will override the current definition.
    https://insert-koin.io/docs/reference/koin-core/modules/#overriding-definition-or-module-310
     */
    val testDataModule = module {
        single<CryptoSignalAlertDB> {
            // https://developer.android.com/training/data-storage/room/testing-db
            Room.inMemoryDatabaseBuilder(
                context, CryptoSignalAlertDB::class.java
            ).build()
        }
    }
}