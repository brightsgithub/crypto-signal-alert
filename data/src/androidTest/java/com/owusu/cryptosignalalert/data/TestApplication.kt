package com.owusu.cryptosignalalert.data

import android.support.multidex.MultiDexApplication
import com.owusu.cryptosignalalert.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TestApplication : MultiDexApplication()  {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@TestApplication)
            modules(listOf(dataModule))
        }
    }
}