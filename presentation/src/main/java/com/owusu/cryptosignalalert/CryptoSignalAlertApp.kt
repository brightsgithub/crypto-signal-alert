package com.owusu.cryptosignalalert

import android.app.Application
import android.support.multidex.MultiDexApplication
//import android.support.multidex.MultiDexApplication
import com.owusu.cryptosignalalert.data.di.DataModuleWrapper
import com.owusu.cryptosignalalert.di.appModule
import com.owusu.cryptosignalalert.di.domainModule
import com.owusu.cryptosignalalert.di.uiModule
import com.owusu.cryptosignalalert.workmanager.Constants.SYNC_PRICES_WORKER_TAG
import com.owusu.cryptosignalalert.workmanager.WorkManagerStarter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
//import org.koin.androidx.viewmodel.BuildConfig
import org.koin.core.context.startKoin

class CryptoSignalAlertApp : MultiDexApplication() {

    private val workManagerStarter: WorkManagerStarter by inject()

    companion object {
        lateinit var instance: CryptoSignalAlertApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin()
        initWorkManager()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@CryptoSignalAlertApp)
            modules(listOf(appModule, uiModule, domainModule, DataModuleWrapper(this@CryptoSignalAlertApp).dataModule))
        }
    }

    // Recommend by android
    // https://developer.android.com/kotlin/coroutines/coroutines-best-practices#create-coroutines-data-layer
    // https://medium.com/androiddevelopers/coroutines-patterns-for-work-that-shouldnt-be-cancelled-e26c40f142ad
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Perodic work stated by application
    // the below was recommend by android: WorkManager Periodicity: https://developer.android.com/topic/libraries/architecture/workmanager/basics
    // https://medium.com/androiddevelopers/workmanager-periodicity-ff35185ff006
    private fun initWorkManager() {
        workManagerStarter.startPeriodicWorker(context = this, SYNC_PRICES_WORKER_TAG)
    }
}