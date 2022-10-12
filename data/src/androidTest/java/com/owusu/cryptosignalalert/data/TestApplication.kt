package com.owusu.cryptosignalalert.data


import android.support.multidex.MultiDexApplication
import com.owusu.cryptosignalalert.data.di.TestDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TestApplication : MultiDexApplication()  {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    /**
     * Overriding definition or module (3.1.0+)
    New Koin override strategy allow to override any definition by default.
    You don't need to specify override = true anymore in your module.
    If you have 2 definitions in different modules, that have the same mapping,
    the last will override the current definition.
    https://insert-koin.io/docs/reference/koin-core/modules/#overriding-definition-or-module-310
     */
    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@TestApplication)
            modules(
                TestDataModule(this@TestApplication).dataModule,
                TestDataModule(this@TestApplication).testDataModule)
        }
    }
}