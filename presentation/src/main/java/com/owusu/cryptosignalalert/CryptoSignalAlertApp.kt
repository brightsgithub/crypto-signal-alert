package com.owusu.cryptosignalalert

import android.support.multidex.MultiDexApplication
import com.owusu.cryptosignalalert.data.di.DataModule
import com.owusu.cryptosignalalert.di.appModule
import com.owusu.cryptosignalalert.di.domainModule
import com.owusu.cryptosignalalert.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.BuildConfig
import org.koin.core.context.startKoin

class CryptoSignalAlertApp : MultiDexApplication() {

    companion object {
        lateinit var instance: CryptoSignalAlertApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@CryptoSignalAlertApp)
            modules(listOf(appModule, uiModule, domainModule, DataModule(this@CryptoSignalAlertApp).getDataModule()))
        }
    }
}